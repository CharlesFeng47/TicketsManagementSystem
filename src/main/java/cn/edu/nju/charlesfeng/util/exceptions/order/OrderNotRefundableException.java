package cn.edu.nju.charlesfeng.util.exceptions.order;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单不可被退订的异常
 * @author Shenmiu
 */
public class OrderNotRefundableException extends MyException {
    public OrderNotRefundableException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
