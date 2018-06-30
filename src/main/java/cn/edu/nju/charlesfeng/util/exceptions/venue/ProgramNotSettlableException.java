package cn.edu.nju.charlesfeng.util.exceptions.venue;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 计划不能被结算时抛出的异常
 * @author Shenmiu
 */
public class ProgramNotSettlableException extends MyException {
    public ProgramNotSettlableException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
