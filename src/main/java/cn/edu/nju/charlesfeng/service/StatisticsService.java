//package cn.edu.nju.charlesfeng.service;
//
//import cn.edu.nju.charlesfeng.model.Consumption;
//import cn.edu.nju.charlesfeng.filter.IncomeOfOneSpot;
//import cn.edu.nju.charlesfeng.filter.NumOfOneMemberLevel;
//import cn.edu.nju.charlesfeng.filter.OrderNumOfOneState;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
//
//import java.util.List;
//
///**
// * 为系统提供统计服务
// */
//public interface StatisticsService {
//
//    /**
//     * @param user 要查看的会员／场馆
//     * @return 会员本人的消费记录／场馆的收支记录
//     */
//    List<Consumption> checkConsumption(User user);
//
//    /**
//     * @param user 要查看的会员／场馆
//     * @return 会员本人的订单数量对比／场馆的订单数量对比
//     */
//    List<OrderNumOfOneState> checkOrders(User user);
//
//    /**
//     * @return 【经理】各会员等级与其数量对比
//     */
//    List<NumOfOneMemberLevel> checkMemberLevels() throws UserNotExistException;
//
//    /**
//     * @return 【经理】所有订单类型的数量对比
//     */
//    List<OrderNumOfOneState> checkMemberOrders();
//
//    /**
//     * @return 【经理】所有场馆的收入情况
//     */
//    List<IncomeOfOneSpot> checkSpotsIncome() throws UserNotExistException;
//}
