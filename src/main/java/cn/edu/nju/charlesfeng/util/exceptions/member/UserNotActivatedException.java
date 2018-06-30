package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户账户未激活
 * @author Shenmiu
 */
public class UserNotActivatedException extends MyException {
    public UserNotActivatedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
