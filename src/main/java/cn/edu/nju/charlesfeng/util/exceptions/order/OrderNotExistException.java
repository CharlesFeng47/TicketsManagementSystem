package cn.edu.nju.charlesfeng.util.exceptions.order;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单不存在
 * @author Shenmiu
 */
public class OrderNotExistException extends MyException {
    public OrderNotExistException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
