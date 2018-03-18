package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentOrder;
import cn.edu.nju.charlesfeng.model.ContentOrderBrief;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 对订单信息的控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private final OrderService orderService;

    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
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
                                     @RequestParam(value = "not_chose_seats") NotChoseSeats notChoseSeats,
                                     @RequestParam(value = "choose_seats_json", required = false, defaultValue = "") String choseSeatListJson,
                                     HttpServletRequest request) {
        logger.debug("INTO /order/save");
        HttpSession session = request.getSession();
        Member curMember = (Member) session.getAttribute(token);

        Order order = orderService.subscribe(curMember, scheduleId, orderType, notChoseSeats, choseSeatListJson);
        return new RequestReturnObject(RequestReturnObjectState.OK, order);
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
        try {
            Order order = orderService.checkOrderDetail(oid);
            Map<String, Spot> spotMap = userService.getAllSpotIdMap();

            ContentOrder result = new ContentOrder(order, spotMap.get(order.getSchedule().getSpotId()));
            return new RequestReturnObject(RequestReturnObjectState.OK, result);

        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }

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
