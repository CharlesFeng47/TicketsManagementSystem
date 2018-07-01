package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户激活链接错误
 */
public class UserActivateUrlWrongException extends MyException {

    public UserActivateUrlWrongException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
