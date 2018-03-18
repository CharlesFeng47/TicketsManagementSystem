package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
     * 获取单条订单
     */
    @GetMapping("/{oid}")
    public RequestReturnObject getOneOrder(@PathVariable("oid") String oidRepre) {
        logger.debug("INTO /order/" + oidRepre);
        return null;
    }
}
