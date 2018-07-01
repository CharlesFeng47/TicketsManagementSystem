package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户激活链接已失效
 * @author Shenmiu
 */
public class UserActivateUrlExpiredException extends MyException {
    public UserActivateUrlExpiredException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
