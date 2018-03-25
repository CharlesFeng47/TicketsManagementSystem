package cn.edu.nju.charlesfeng.util.enums;

import java.io.Serializable;

/**
 * 标志 RESTful 风格请求时返回的结果状态
 */
public enum RequestReturnObjectState implements Serializable {

    // 成功
    OK(200),
    // 用户未注册
    USER_NOT_EXIST(1),
    // 用户密码错误
    USER_PWD_WRONG(2),
    // 内部计算错误
    INTERIOR_WRONG(3),
    // 会员名已被注册
    USER_HAS_BEEN_SIGN_UP(4),
    // 参数缺失
    ORDER_MEMBER_ID_MISS(5),
    // 优惠券兑换时积分余额不足
    COUPON_CONVERT_CREDIT_NOT_ENOUGH(6),
    // 支付宝账户余额不足
    PAY_BALANCE_NOT_ADEQUATE(7),
    // 支付宝账户密码错误
    PAY_WRONG_PWD(8),
    // 订单已经被检票登记过了
    TICKET_HAS_BEEN_CHECKED(9),
    // 订单检票登记时的其他异常
    TICKET_STATE_WRONG(10),
    // 订单不存在
    TICKET_NOT_EXIST(11),
    // 会员已注销
    MEMBER_INVALIDATE(12),
    // 会员账号未激活
    MEMBER_INACTIVE(13),
    // 计划不能被结算
    SCHEDULE_NOT_SEETLABLE(14),
    // 支付宝账号不存在
    ALIPAY_ENTITY_NOT_EXIST(15),
    // 订单不可被退款
    ORDER_NOT_REFUNDABLE(16),
    // 用户链接错误
    MEMBER_ACTIVATE_URL_EXPIRE(17),
    // 用户链接错误
    MEMBER_ACTIVATE_URL_WRONG(18);

    private int repre;

    RequestReturnObjectState(int repre) {
        this.repre = repre;
    }
}
