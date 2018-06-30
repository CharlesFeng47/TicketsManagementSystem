package cn.edu.nju.charlesfeng.util.exceptions.ticket;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 余票数量不足
 * @author Shenmiu
 */
public class TicketsNotAdequateException extends MyException {
    public TicketsNotAdequateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
