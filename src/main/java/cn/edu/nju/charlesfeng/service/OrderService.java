package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.*;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotCancelException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotPaymentException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotRefundableException;
import cn.edu.nju.charlesfeng.util.exceptions.pay.AlipayBalanceNotAdequateException;
import cn.edu.nju.charlesfeng.util.exceptions.member.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.member.WrongPwdException;

import java.util.List;

/**
 * 系统中订单的服务
 */
public interface OrderService {

    /**
     * @param orderID 要查看的订单ID
     * @return 订单详情
     */
    Order checkOrderDetail(OrderID orderID);

    /**
     * 生成订单
     *
     * @param order 订单实体
     * @return 保存结果
     */
    boolean generateOrder(Order order);

    /**
     * 取消订单
     *
     * @param orderID 订单ID
     * @return 是否成功取消
     */
    boolean cancelOrder(OrderID orderID) throws OrderNotCancelException;

    /**
     * 取消订单(系统调度取消)
     *
     * @param order 订单
     * @return 是否成功取消
     */
    boolean cancelOrderBySchedule(Order order);

    /**
     * 订单支付
     *
     * @param orderID 订单ID
     * @return 是否成功支付
     */
    boolean payOrder(OrderID orderID) throws OrderNotPaymentException, UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException;

    /**
     * 订单退订
     *
     * @param orderID 订单ID
     * @return 是否成功退订
     */
    boolean unsubscribe(OrderID orderID) throws OrderNotRefundableException, UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException;

    /**
     * @param uid 查看订单的用户
     * @return 查看某一用户的全部订单
     */
    List<Order> getMyOrders(String uid);

    /**
     * @param uid        查看指定类型订单的用户
     * @param orderState 订单状态或订单类型
     * @return 查看某一用户的全部订单
     */
    List<Order> getMyOrders(String uid, OrderState orderState);

    /**
     * 跟据订单状态获取订单
     *
     * @param orderState
     * @return
     */
    List<Order> getOrderByState(OrderState orderState);

//    /**
//     * @param spot 检票登记的主动方场馆
//     * @param oid  检票登记的订单号
//     * @return 检票结果，成功则true
//     */
//    boolean checkTicket(Spot spot, int oid) throws TicketHasBeenCheckedException, TicketStateWrongException, OrderNotExistException, TicketCheckerWrongException;


//    /**
//     * 预定计划
//     *
//     * @param curUser        预定的用户
//     * @param scheduleId     预定的计划
//     * @param orderType      订购类型
//     * @param notChoseSeats  不选座订购时的座位情况
//     * @param choseSeatsJson 选座订购时的座位情况Json
//     * @param orderWay       订座方式
//     * @param onSpotIsMember 场馆现场购票是是否使用会员编号积分
//     * @param onSpotMemberId 场馆现场购票时的会员编号
//     * @param didUseCoupon   订座时是否使用优惠券
//     * @param usedCoupon     订购时使用的优惠券
//     * @param calProcess     订购是价格的计算过程
//     * @param totalPrice     订单总价
//     * @return 预定结果，成果则返回订单实体
//     */
//    int subscribe(User curUser, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
//                  String choseSeatsJson, OrderWay orderWay, boolean onSpotIsMember, String onSpotMemberId,
//                  boolean didUseCoupon, Coupon usedCoupon, String calProcess, double totalPrice) throws UserNotExistException, InteriorWrongException;

}
