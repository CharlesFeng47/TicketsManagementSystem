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
    public RequestReturnObject getMyOrdersByState(@RequestParam("orderType") String type, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /order/getMyOrdersByState" + token + type);
        OrderState orderState = OrderState.getEnum(type);
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
    public RequestReturnObject generateOrder(@RequestParam("program_id") String program_id, @RequestParam("seatType") String seatType,
                                             @RequestParam("programTime") String programTime, @RequestParam("ticket_num") int num,
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
            String ids[] = program_id.split("-");
            ProgramID programID = new ProgramID();
            programID.setVenueID(Integer.parseInt(ids[0]));
            programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(ids[1])));
            List<Ticket> tickets = ticketService.lock(programID, num, seatType); //进行锁票,后面加锁
            OrderID orderID = new OrderID();
            orderID.setTime(TimeHelper.standardTime(LocalDateTime.now()));
            orderID.setEmail(token);
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
    public RequestReturnObject cancelOrder(@RequestParam("orderID") String order_id, @SessionAttribute("user_id") String userID) {
        logger.debug("INTO /order/generateOrder" + userID);
        try {
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(order_id));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(userID);
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
    public RequestReturnObject unsubscribeOrder(@RequestParam("orderID") String order_id, @SessionAttribute("user_id") String userID) {
        logger.debug("INTO /order/generateOrder" + userID);
        try {
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(order_id));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(userID);
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
    public RequestReturnObject payOrder(@RequestParam("orderID") String order_id, @SessionAttribute("user_id") String userID) {
        logger.debug("INTO /order/generateOrder" + userID);
        try {
            LocalDateTime orderTime = TimeHelper.getLocalDateTime(Long.parseLong(order_id));
            OrderID orderID = new OrderID();
            orderID.setTime(orderTime);
            orderID.setEmail(userID);
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

//    /**
//     * 获取所有订单的简历
//     */
//    @GetMapping("/all")
//    public RequestReturnObject getAllOrders(String mid) {
//        if (mid == null || mid.equals("")) {
//            return new RequestReturnObject(RequestReturnObjectState.ORDER_MEMBER_ID_MISS);
//        } else {
//            logger.debug("INTO /order/all?mid=" + mid);
//            List<Order> allOrders = orderService.getMyOrders(mid);
//            return new RequestReturnObject(RequestReturnObjectState.OK, getBrief(allOrders));
//        }
//    }


//    /**
//     * 支付订单
//     */
//    @PostMapping("/pay")
//    public RequestReturnObject payOrder(@RequestParam("token") String token, @RequestParam("oid") int oid,
//                                        @RequestParam("payment_id") String id, @RequestParam("payment_pwd") String pwd,
//                                        HttpServletRequest request) {
//        logger.debug("INTO /order/pay");
//        HttpSession session = request.getSession();
////        Member curMember = (Member) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Member;
//        Member curMember = (Member) o;
//
//        try {
//            orderService.payOrder(curMember, oid, id, pwd);
//            return new RequestReturnObject(RequestReturnObjectState.OK);
//        } catch (AlipayWrongPwdException e) {
//            return new RequestReturnObject(RequestReturnObjectState.PAY_WRONG_PWD);
//        } catch (AlipayBalanceNotAdequateException e) {
//            return new RequestReturnObject(RequestReturnObjectState.PAY_BALANCE_NOT_ADEQUATE);
//        }
//    }

//    /**
//     * 订单退订
//     */
//    @PostMapping("/unsubscribe")
//    public RequestReturnObject unsubscribeOrder(@RequestParam("token") String token, @RequestParam("oid") int oid,
//                                                @RequestParam("payment_id") String id, HttpServletRequest request) {
//        logger.debug("INTO /order/unsubscribe");
//        HttpSession session = request.getSession();
////        Member curMember = (Member) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Member;
//        Member curMember = (Member) o;
//
//        try {
//            orderService.unsubscribe(curMember, oid, id);
//            return new RequestReturnObject(RequestReturnObjectState.OK);
//        } catch (InteriorWrongException e) {
//            e.printStackTrace();
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        } catch (OrderNotRefundableException e) {
//            return new RequestReturnObject(RequestReturnObjectState.ORDER_NOT_REFUNDABLE);
//        } catch (AlipayEntityNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.ALIPAY_ENTITY_NOT_EXIST);
//        }
//    }

//    /**
//     * @return 获取订单对应的简介
//     */
//    private List<ContentOrderBrief> getBrief(List<Order> orders) {
//        List<ContentOrderBrief> result = new LinkedList<>();
//        for (Order order : orders) {
//            result.add(new ContentOrderBrief(order));
//        }
//        return result;
//    }
//
//    /**
//     * 将界面展示的八位oid变为整数
//     */
//    private int getOidInteger(String oid) {
//        return Integer.parseInt(oid);
//    }
}
