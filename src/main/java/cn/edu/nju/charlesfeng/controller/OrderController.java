package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.dto.order.OrderDTO;
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
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.exceptions.member.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.member.WrongPwdException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotCancelException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotCreateException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotPaymentException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotRefundableException;
import cn.edu.nju.charlesfeng.util.exceptions.pay.AlipayBalanceNotAdequateException;
import cn.edu.nju.charlesfeng.util.exceptions.ticket.TicketsNotAdequateException;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 对订单信息的控制器
 * @author Dong
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
    public OrderDTO getOneOrder(@RequestParam("order_time") long time, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/getOneOrder" + token + time);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        OrderID orderID = new OrderID();
        orderID.setEmail(user.getEmail());
        orderID.setTime(TimeHelper.getLocalDateTime(time));
        Order order = orderService.checkOrderDetail(orderID);
        return new OrderDTO(order);
    }

    /**
     * 获取指定类型订单
     */
    @PostMapping("/getMyOrdersByState")
    public List<OrderDTO> getMyOrdersByState(@RequestParam("order_type") String orderType, @RequestParam("token") String token, HttpServletRequest request) {
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
        List<OrderDTO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(new OrderDTO(order));
        }
        return result;
    }

    /**
     * 下订单（立即购买）
     */
    @PostMapping("/generateOrder")
    public Long generateOrder(@RequestParam("program_id") String programIDString, @RequestParam("seat_type") String seatType,
                              @RequestParam("program_time") String programTime, @RequestParam("ticket_num") int num,
                              @RequestParam("token") String token, HttpServletRequest request) throws TicketsNotAdequateException, OrderNotCreateException {
        logger.debug("INTO /order/generateOrder" + token);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(programTime, df);
        if (time.plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new OrderNotCreateException(ExceptionCode.ORDER_NOT_CREATE);
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        String ids[] = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(ids[0]));
        programID.setStartTime(LocalDateTime.parse(programTime.replace(" ", "T")));
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
        return TimeHelper.getLong(order.getOrderID().getTime());
    }

    /**
     * 订单取消
     */
    @PostMapping("/cancelOrder")
    public void cancelOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) throws OrderNotCancelException {
        logger.debug("INTO /order/generateOrder" + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
        OrderID orderID = new OrderID();
        orderID.setTime(orderTime);
        orderID.setEmail(user.getEmail());
        orderService.cancelOrder(orderID);
    }

    /**
     * 订单退订
     */
    @PostMapping("/unsubscribeOrder")
    public void unsubscribeOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) throws WrongPwdException, OrderNotRefundableException, AlipayBalanceNotAdequateException, UserNotExistException {
        logger.debug("INTO /order/generateOrder" + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
        OrderID orderID = new OrderID();
        orderID.setTime(orderTime);
        orderID.setEmail(user.getEmail());
        orderService.unsubscribe(orderID);
    }

    /**
     * 订单支付
     */
    @PostMapping("/payOrder")
    public void payOrder(@RequestParam("order_id") String orderNum, @RequestParam("token") String token, HttpServletRequest request) throws WrongPwdException, OrderNotPaymentException, AlipayBalanceNotAdequateException, UserNotExistException {
        logger.debug("INTO /order/generateOrder" + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(orderNum));
        OrderID orderID = new OrderID();
        orderID.setTime(orderTime);
        orderID.setEmail(user.getEmail());
        orderService.payOrder(orderID);
    }
}
