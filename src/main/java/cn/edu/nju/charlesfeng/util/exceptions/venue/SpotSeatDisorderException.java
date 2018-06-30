package cn.edu.nju.charlesfeng.util.exceptions.venue;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 场馆座位顺序错乱导致的异常
 * @author Shenmiu
 */
public class SpotSeatDisorderException extends MyException {
    public SpotSeatDisorderException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
