//package cn.edu.nju.charlesfeng.controller;
//
//import cn.edu.nju.charlesfeng.model.Consumption;
//import cn.edu.nju.charlesfeng.model.Manager;
//import cn.edu.nju.charlesfeng.filter.*;
//import cn.edu.nju.charlesfeng.service.StatisticsService;
//import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
//import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * 对统计数据提供的控制器
// */
//@RestController
//@RequestMapping("/statistics")
//public class StatisticsController {
//
//    private static final Logger logger = Logger.getLogger(UserController.class);
//
//    private final StatisticsService statisticsService;
//
//    @Autowired
//    public StatisticsController(StatisticsService statisticsService) {
//        this.statisticsService = statisticsService;
//    }
//
//    /**
//     * 统计消费情况
//     */
//    @PostMapping("consumption")
//    public RequestReturnObject getConsumption(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /statistics/consumption");
//
//        HttpSession session = request.getSession();
////        User curUser = (User) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof User;
//        User curUser = (User) o;
//
//
//        List<Consumption> result = statisticsService.checkConsumption(curUser);
//        return new RequestReturnObject(RequestReturnObjectState.OK, convertToContentConsumption(result));
//    }
//
//    /**
//     * 统计订单情况
//     */
//    @PostMapping("orders")
//    public RequestReturnObject getOrdersStatistics(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /statistics/orders");
//
//        HttpSession session = request.getSession();
////        User curUser = (User) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof User;
//        User curUser = (User) o;
//
//        List<OrderNumOfOneState> result = statisticsService.checkOrders(curUser);
//        return new RequestReturnObject(RequestReturnObjectState.OK, result);
//    }
//
//    /**
//     * 经理统计场馆情况
//     */
//    @PostMapping("spots")
//    public RequestReturnObject getSpotsStatistics(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /statistics/spots");
//
//        HttpSession session = request.getSession();
//        Object curUser = session.getAttribute(token);
//        assert curUser != null && curUser instanceof Manager;
//
//        try {
//            List<IncomeOfOneSpot> result = statisticsService.checkSpotsIncome();
//            return new RequestReturnObject(RequestReturnObjectState.OK, result);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }
//
//    /**
//     * 经理统计会员等级情况
//     */
//    @PostMapping("member_level")
//    public RequestReturnObject getMemberLevelStatistics(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /statistics/members_level");
//
//        HttpSession session = request.getSession();
//        Object curUser = session.getAttribute(token);
//        assert curUser != null && curUser instanceof Manager;
//
//        try {
//            List<NumOfOneMemberLevel> result = statisticsService.checkMemberLevels();
//            return new RequestReturnObject(RequestReturnObjectState.OK, result);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }
//
//    /**
//     * 经理统计会员订单情况
//     */
//    @PostMapping("member_order")
//    public RequestReturnObject getMemberOrderStatistics(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /statistics/members_level");
//
//        HttpSession session = request.getSession();
//        Object curUser = session.getAttribute(token);
//        assert curUser != null && curUser instanceof Manager;
//
//        List<OrderNumOfOneState> result = statisticsService.checkMemberOrders();
//        return new RequestReturnObject(RequestReturnObjectState.OK, result);
//    }
//
//
//    private List<ContentConsumption> convertToContentConsumption(List<Consumption> consumptions) {
//        List<ContentConsumption> result = new LinkedList<>();
//        for (Consumption cur : consumptions) {
//            result.add(new ContentConsumption(cur));
//        }
//        return result;
//    }
//}
