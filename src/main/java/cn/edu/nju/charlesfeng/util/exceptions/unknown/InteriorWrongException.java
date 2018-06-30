package cn.edu.nju.charlesfeng.util.exceptions.unknown;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 为了程序可扩展性，一些地方写的比较活，此异常即出现在不应该出现的地方
 * @author Shenmiu
 */
public class InteriorWrongException extends MyException {
    public InteriorWrongException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public InteriorWrongException(ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
