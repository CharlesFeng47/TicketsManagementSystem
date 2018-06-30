package cn.edu.nju.charlesfeng.util.exceptions.order;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * @author Shenmiu
 * @date 2018/06/30
 *
 * 不可生成订单（生成时间晚于节目开始前15分钟）
 */
public class OrderNotCreateException extends MyException {
    public OrderNotCreateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
