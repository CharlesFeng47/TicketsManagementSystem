package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.OrderWay;
import cn.edu.nju.charlesfeng.util.exceptions.*;

import java.util.List;

/**
 * 系统中订单的服务
 */
public interface OrderService {

    /**
     * 预定计划
     *
     * @param curUser        预定的用户
     * @param scheduleId     预定的计划
     * @param orderType      订购类型
     * @param notChoseSeats  不选座订购时的座位情况
     * @param choseSeatsJson 选座订购时的座位情况Json
     * @param orderWay       订座方式
     * @param onSpotIsMember 场馆现场购票是是否使用会员编号积分
     * @param onSpotMemberId 场馆现场购票时的会员编号
     * @param didUseCoupon   订座时是否使用优惠券
     * @param usedCoupon     订购时使用的优惠券
     * @param calProcess     订购是价格的计算过程
     * @param totalPrice     订单总价
     * @return 预定结果，成果则返回订单实体
     */
    int subscribe(User curUser, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
                  String choseSeatsJson, OrderWay orderWay, boolean onSpotIsMember, String onSpotMemberId,
                  boolean didUseCoupon, Coupon usedCoupon, String calProcess, double totalPrice) throws UserNotExistException, InteriorWrongException;

    // TODO 付款后增加积分

    /**
     * @param member     支付的用户
     * @param oid        支付的订单编号
     * @param paymentId  支付的账号
     * @param paymentPwd 支付的密码
     * @return 支付结果，成功则true
     */
    boolean payOrder(Member member, int oid, String paymentId, String paymentPwd) throws AlipayWrongPwdException, AlipayBalanceNotAdequateException;

    /**
     * @param oid 检票登记的订单号
     * @return 检票结果，成功则true
     */
    boolean checkTicket(int oid) throws TicketHasBeenCheckedException, TicketStateWrongException, OrderNotExistException;

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
