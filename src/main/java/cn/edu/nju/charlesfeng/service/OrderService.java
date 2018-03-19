package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.util.enums.OrderType;

import java.util.List;

/**
 * 系统中订单的服务
 */
public interface OrderService {

    /**
     * 预定计划
     *
     * @param member         预定的用户
     * @param scheduleId     预定的计划
     * @param orderType      订购类型
     * @param notChoseSeats  不选座订购时的座位情况
     * @param choseSeatsJson 选座订购时的座位情况Json
     * @param didUseCoupon   订座时是否使用优惠券
     * @param usedCoupon     订购时使用的优惠券
     * @param totalPrice     订单总价
     * @return 预定结果，成果则返回订单实体
     */
    Order subscribe(Member member, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
                    String choseSeatsJson, boolean didUseCoupon, Coupon usedCoupon, double totalPrice);

    /**
     * @param mid 查看订单的会员
     * @return 查看某一会员的全部ing单
     */
    List<Order> getMyOrders(String mid);

    /**
     * @param oid 要查看的订单ID
     * @return 订单详情
     */
    Order checkOrderDetail(int oid);

    /**
     * @param oid 要退订的订单ID
     * @return 退订结果，成功则true
     */
    boolean unsubscribe(String oid);
}
