package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对订单信息的控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 获取单个订单
     */
    @GetMapping("/getOneOrder")
    public RequestReturnObject getOneOrder(@RequestParam("order_time") LocalDateTime time, @SessionAttribute("user_id") String userID) {
        logger.debug("INTO /order/getOneOrder" + userID + time);
        OrderID orderID = new OrderID();
        orderID.setEmail(userID);
        orderID.setTime(time);
        Order order = orderService.checkOrderDetail(orderID);
        return new RequestReturnObject(RequestReturnObjectState.OK, order);
    }

    /**
     * 获取单个订单（确认订单操作时）TODO 暂时无法测试，顶层尚未有订单
     */
    @GetMapping("/getOneOrderForConfirm")
    @FastJsonView(include = @FastJsonFilter(clazz = Order.class, props = {"orderID", "totalPrice", "program.name", "program.poster", "program.venue.address", "tickets"}))
    public RequestReturnObject getOneOrderForConfirm(@RequestParam("order_time") LocalDateTime time, @SessionAttribute("user_id") String userID) {
        logger.debug("INTO /order/getOneOrder" + userID + time);
        OrderID orderID = new OrderID();
        orderID.setEmail(userID);
        orderID.setTime(time);
        Order order = orderService.checkOrderDetail(orderID);
        return new RequestReturnObject(RequestReturnObjectState.OK, order);
    }


//    /**
//     * 下达订单订购
//     *
//     * @param token             用户标志
//     * @param orderType         订购类型
//     * @param notChoseSeats     不选座时订座信息
//     * @param choseSeatListJson 选座时的座位具体情况JSON串
//     */
//    @PostMapping("/save")
//    public RequestReturnObject order(@RequestParam("token") String token,
//                                     @RequestParam("scheduleId") String scheduleId,
//                                     @RequestParam("order_type") OrderType orderType,
//                                     @RequestParam("not_chose_seats") NotChoseSeats notChoseSeats,
//                                     @RequestParam(value = "choose_seats_json", required = false, defaultValue = "") String choseSeatListJson,
//                                     @RequestParam("order_way") OrderWay orderWay,
//                                     @RequestParam("on_spot_is_member") boolean onSpotIsMember,
//                                     @RequestParam("on_spot_member_id") String onSpotMemberId,
//                                     @RequestParam(value = "order_did_use_coupon") boolean didUseCoupon,
//                                     @RequestParam(value = "order_used_coupon") Coupon usedCoupon,
//                                     @RequestParam("order_cal_process") String calProcess,
//                                     @RequestParam("order_total_price") double totalPrice,
//                                     HttpServletRequest request) {
//        logger.debug("INTO /order/save");
//        HttpSession session = request.getSession();
////        User curUser = (User) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof User;
//        User curUser = (User) o;
//        try {
//            int orderId = orderService.subscribe(curUser, scheduleId, orderType, notChoseSeats, choseSeatListJson,
//                    orderWay, onSpotIsMember, onSpotMemberId, didUseCoupon, usedCoupon, calProcess, totalPrice);
//            return new RequestReturnObject(RequestReturnObjectState.OK, orderId);
//        } catch (UserNotExistException | InteriorWrongException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }

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
