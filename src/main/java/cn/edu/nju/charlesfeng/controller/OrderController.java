package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.model.ContentOrder;
import cn.edu.nju.charlesfeng.model.ContentOrderBrief;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.OrderWay;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.exceptions.InteriorWrongException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

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
     * 下达订单订购
     *
     * @param token             用户标志
     * @param orderType         订购类型
     * @param notChoseSeats     不选座时订座信息
     * @param choseSeatListJson 选座时的座位具体情况JSON串
     */
    @PostMapping("/save")
    public RequestReturnObject order(@RequestParam("token") String token,
                                     @RequestParam("scheduleId") String scheduleId,
                                     @RequestParam("order_type") OrderType orderType,
                                     @RequestParam("not_chose_seats") NotChoseSeats notChoseSeats,
                                     @RequestParam(value = "choose_seats_json", required = false, defaultValue = "") String choseSeatListJson,
                                     @RequestParam("order_way") OrderWay orderWay,
                                     @RequestParam("on_spot_is_member") boolean onSpotIsMember,
                                     @RequestParam("on_spot_member_id") String onSpotMemberId,
                                     @RequestParam(value = "order_did_use_coupon") boolean didUseCoupon,
                                     @RequestParam(value = "order_used_coupon") Coupon usedCoupon,
                                     @RequestParam("order_cal_process") String calProcess,
                                     @RequestParam("order_total_price") double totalPrice,
                                     HttpServletRequest request) {
        logger.debug("INTO /order/save");
        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute(token);

        try {
            Order order = orderService.subscribe(curUser, scheduleId, orderType, notChoseSeats, choseSeatListJson,
                    orderWay, onSpotIsMember, onSpotMemberId, didUseCoupon, usedCoupon, calProcess, totalPrice);
            return new RequestReturnObject(RequestReturnObjectState.OK, order);
        } catch (UserNotExistException | InteriorWrongException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }

    /**
     * 获取所有订单的简历
     */
    @GetMapping("/all")
    public RequestReturnObject getAllOrders(String mid) {
        if (mid == null || mid.equals("")) {
            return new RequestReturnObject(RequestReturnObjectState.ORDER_MEMBER_ID_MISS);
        } else {
            logger.debug("INTO /order/all?mid=" + mid);
            List<Order> allOrders = orderService.getMyOrders(mid);
            return new RequestReturnObject(RequestReturnObjectState.OK, getBrief(allOrders));
        }
    }

    /**
     * 获取单条订单
     */
    @GetMapping("/{oid}")
    public RequestReturnObject getOneOrder(@PathVariable("oid") int oid) {
        logger.debug("INTO /order/" + oid);
        Order order = orderService.checkOrderDetail(oid);
        ContentOrder result = new ContentOrder(order);
        return new RequestReturnObject(RequestReturnObjectState.OK, result);


    }

    /**
     * @return 获取订单对应的简介
     */
    private List<ContentOrderBrief> getBrief(List<Order> orders) {
        List<ContentOrderBrief> result = new LinkedList<>();
        for (Order order : orders) {
            result.add(new ContentOrderBrief(order));
        }
        return result;
    }
}
