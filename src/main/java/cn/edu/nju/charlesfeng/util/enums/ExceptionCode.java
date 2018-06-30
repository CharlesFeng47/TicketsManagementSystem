package cn.edu.nju.charlesfeng.util.enums;

import java.io.Serializable;

/**
 * 标志 RESTful 风格请求时返回的结果状态
 */
public enum ExceptionCode implements Serializable {

    // 成功
    OK(200, "成功"),
    // 用户未注册
    USER_NOT_EXIST(1, "用户未注册"),
    // 用户密码错误
    USER_PWD_WRONG(2, "用户密码错误"),
    // 内部计算错误
    INTERIOR_WRONG(3, "内部计算错误"),
    // 该邮箱已被注册
    USER_HAS_BEEN_SIGN_UP(4, "该邮箱已被注册"),
    // 参数缺失
    ORDER_MEMBER_ID_MISS(5, "参数缺失"),
    // 优惠券兑换时积分余额不足
    COUPON_CONVERT_CREDIT_NOT_ENOUGH(6, "优惠券兑换时积分余额不足"),
    // 支付宝账户余额不足
    PAY_BALANCE_NOT_ADEQUATE(7, "支付宝账户余额不足"),
    // 支付宝账户密码错误
    PAY_WRONG_PWD(8, "支付宝账户密码错误"),
    // 订单已经被检票登记过了
    TICKET_HAS_BEEN_CHECKED(9, "订单已经被检票登记过了"),
    // 订单检票登记时的其他异常
    TICKET_STATE_WRONG(10, "订单检票登记时的其他异常"),
    // 订单不存在
    TICKET_NOT_EXIST(11, "订单不存在"),
    // 用户账号未激活
    USER_INACTIVE(12, "用户账号未激活"),
    // 计划不能被结算
    SCHEDULE_NOT_SEETLABLE(13, "计划不能被结算"),
    // 支付宝账号不存在
    ALIPAY_ENTITY_NOT_EXIST(14, "支付宝账号不存在"),
    // 订单不可被退款
    ORDER_NOT_REFUNDABLE(15, "订单不可被退款"),
    // 用户链接错误
    MEMBER_ACTIVATE_URL_EXPIRE(16, "用户链接错误"),
    // 用户链接错误
    MEMBER_ACTIVATE_URL_WRONG(17, "用户链接错误"),
    // 订单检票方不是订单中的场馆
    TICKET_CHECKER_WRONG(18, "订单检票方不是订单中的场馆"),
    // 不可生成订单（生成时间晚于节目开始前15分钟）
    ORDER_NOT_CREATE(19, "不可生成订单（生成时间晚于节目开始前15分钟）"),
    // 订单不可支付
    ORDER_NOT_PAYMENT(20, "订单不可支付"),
    // 订单不可取消
    ORDER_NOT_CANCEL(21, "订单不可取消"),
    // 余票不足
    TICKET_NOT_ADEQUATE(22, "余票不足"),
    // 请求没有传送token
    TOKEN_IS_NULL(23, "请求没有传送token"),
    // 节目ID格式不对
    WRONG_PROGRAM_ID(24, "节目ID格式不对");

    private int repre;

    private String message;

    ExceptionCode(int repre, String message) {
        this.repre = repre;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRepre() {
        return repre;
    }

    public void setRepre(int repre) {
        this.repre = repre;
    }
}
