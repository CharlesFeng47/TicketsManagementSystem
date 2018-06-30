package cn.edu.nju.charlesfeng.util.exceptions.ticket;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 订单已经被检票登记过了
 * @author Shenmiu
 */
public class TicketHasBeenCheckedException extends MyException {
    public TicketHasBeenCheckedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
