package cn.edu.nju.charlesfeng.util.exceptions.order;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单不可被支付的异常
 */
public class OrderNotPaymentException extends MyException {
    public OrderNotPaymentException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
