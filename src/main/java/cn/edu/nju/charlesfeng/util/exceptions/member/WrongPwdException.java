package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户登录时密码错误
 * @author Shenmiu
 */
public class WrongPwdException extends MyException {
    public WrongPwdException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
