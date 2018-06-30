package cn.edu.nju.charlesfeng.util.exceptions.venue;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 配票的时候无合适座位
 * @author Shenmiu
 */
public class NoSuitableSeatException extends MyException {
    public NoSuitableSeatException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
