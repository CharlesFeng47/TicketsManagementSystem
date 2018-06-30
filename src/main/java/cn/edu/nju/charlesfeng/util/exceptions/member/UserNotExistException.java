package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户不存在
 * @author Shenmiu
 */
public class UserNotExistException extends MyException {
    public UserNotExistException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public UserNotExistException(ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
