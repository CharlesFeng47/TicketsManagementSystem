package cn.edu.nju.charlesfeng.util.exceptions.ticket;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单检票时，除正常支付、已检票之外其他的情况
 * @author Shenmiu
 */
public class TicketStateWrongException extends MyException {
    public TicketStateWrongException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
