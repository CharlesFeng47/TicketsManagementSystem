package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 用户激活链接已失效
 * @author Shenmiu
 */
public class UserActiveUrlExpiredException extends MyException {
    public UserActiveUrlExpiredException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
