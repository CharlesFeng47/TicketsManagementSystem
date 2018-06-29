package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.service.ParService;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import cn.edu.nju.charlesfeng.util.filter.order.OrderBrief;
import cn.edu.nju.charlesfeng.util.filter.order.OrderDetail;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 对订单信息的控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private final OrderService orderService;

    private final ProgramService programService;

    private final ParService parService;

    private final TicketService ticketService;

    @Autowired
    public OrderController(OrderService orderService, ProgramService programService, ParService parService, TicketService ticketService) {
        this.orderService = orderService;
        this.programService = programService;
        this.parService = parService;
        this.ticketService = ticketService;
    }

    /**
     * 获取单个订单详情
     */
    @PostMapping("/getOneOrder")
    public RequestReturnObject getOneOrder(@RequestParam("order_time") long time, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/getOneOrder" + token + time);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        OrderID orderID = new OrderID();
        orderID.setEmail(user.getEmail());
        orderID.setTime(TimeHelper.getLocalDateTime(time));
        Order order = orderService.checkOrderDetail(orderID);
        return new RequestReturnObject(RequestReturnObjectState.OK, new OrderDetail(order));
    }

    /**
     * 获取指定类型订单
     */
    @PostMapping("/getMyOrdersByState")
    public RequestReturnObject getMyOrdersByState(@RequestParam("order_type") String orderType, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/getMyOrdersByState" + token + orderType);
        OrderState orderState = OrderState.getEnum(orderType);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        List<Order> orders = null;
        if (orderState.equals(OrderState.ALL)) {
            orders = orderService.getMyOrders(user.getEmail());
        } else {
            orders = orderService.getMyOrders(user.getEmail(), orderState);
        }
        List<OrderBrief> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(new OrderBrief(order));
        }
        return new RequestReturnObject(RequestReturnObjectState.OK, result);
    }

    /**
     * 下订单（立即购买）
     */
    @PostMapping("/generateOrder")
    public RequestReturnObject generateOrder(@RequestParam("program_id") String programIDString, @RequestParam("seat_type") String seatType,
                                             @RequestParam("program_time") String programTime, @RequestParam("ticket_num") int num,
                                             @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/generateOrder" + token);
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.parse(programTime, df);
            if (time.plusMinutes(15).isBefore(LocalDateTime.now())) {
                return new RequestReturnObject(RequestReturnObjectState.ORDER_NOT_CREATE);
            }

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            String ids[] = programIDString.split("-");
            ProgramID programID = new ProgramID();
            programID.setVenueID(Integer.parseInt(ids[0]));
            programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(ids[1])));
            List<Ticket> tickets = ticketService.lock(programID, num, seatType); //进行锁票,后面加锁
            OrderID orderID = new OrderID();
            orderID.setTime(TimeHelper.standardTime(LocalDateTime.now()));
            orderID.setEmail(user.getEmail());
            Order order = new Order();
            order.setOrderID(orderID);
            double price = parService.getSeatPrice(programID, seatType);
            order.setTotalPrice(price * num);
            order.setOrderState(OrderState.UNPAID);
            for (Ticket ticket : tickets) {
                ticket.setOrder(order);
            }
            Program program = programService.getOneProgram(programID);
            program.getOrders().add(order);
            order.setProgramID(program.getProgramID());
            order.setProgram(program);
            order.setTickets(new HashSet<>(tickets)); //关联订单
            orderService.generateOrder(order);
            return new RequestReturnObject(RequestReturnObjectState.OK, TimeHelper.getLong(order.getOrderID().getTime()));
        } catch (TicketsNotAdequateException e) {
            return new RequestReturnObject(RequestReturnObjectState.OK, RequestReturnObjectState.TICKET_NOT_ADEQUATE);
        }
    }

    /**
     * 订单取消
     */
    @PostMapping("/cancelOrder")
    public RequestReturnObject cancelOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/generateOrder" + token);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(user.getEmail());
            orderService.cancelOrder(orderID);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } catch (OrderNotCancelException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.ORDER_NOT_CANCEL);
        }
    }

    /**
     * 订单退订
     */
    @PostMapping("/unsubscribeOrder")
    public RequestReturnObject unsubscribeOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/generateOrder" + token);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(user.getEmail());
            orderService.unsubscribe(orderID);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } catch (UserNotExistException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        } catch (OrderNotRefundableException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.ORDER_NOT_REFUNDABLE);
        } catch (WrongPwdException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.PAY_WRONG_PWD);
        } catch (AlipayBalanceNotAdequateException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.PAY_BALANCE_NOT_ADEQUATE);
        }
    }

    /**
     * 订单支付
     */
    @PostMapping("/payOrder")
    public RequestReturnObject payOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/generateOrder" + token);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(user.getEmail());
            orderService.payOrder(orderID);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } catch (OrderNotPaymentException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.ORDER_NOT_PAYMENT);
        } catch (UserNotExistException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        } catch (WrongPwdException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.PAY_WRONG_PWD);
        } catch (AlipayBalanceNotAdequateException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.PAY_BALANCE_NOT_ADEQUATE);
        }
    }
}
