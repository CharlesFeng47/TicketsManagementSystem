package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户的 token 已经失效
 */
public class UserTokenExpiredException extends MyException {

    public UserTokenExpiredException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
