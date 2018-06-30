package cn.edu.nju.charlesfeng.util.exceptions.ticket;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单检票方不是订单的场馆
 * @author Shenmiu
 */
public class TicketCheckerWrongException extends MyException {
    public TicketCheckerWrongException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
