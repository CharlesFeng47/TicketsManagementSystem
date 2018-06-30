package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 会员名已经备注过
 * @author Shenmiu
 */
public class UserHasBeenSignUpException extends MyException {
    public UserHasBeenSignUpException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
