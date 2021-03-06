package cn.edu.nju.charlesfeng.util.exceptions.order;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单不可被取消的异常
 * @author Shenmiu
 */
public class OrderNotCancelException extends MyException {
    public OrderNotCancelException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
