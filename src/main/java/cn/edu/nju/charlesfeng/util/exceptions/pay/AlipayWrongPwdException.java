package cn.edu.nju.charlesfeng.util.exceptions.pay;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 支付宝支付时密码错误
 * @author Shenmiu
 */
public class AlipayWrongPwdException extends MyException {
    public AlipayWrongPwdException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
