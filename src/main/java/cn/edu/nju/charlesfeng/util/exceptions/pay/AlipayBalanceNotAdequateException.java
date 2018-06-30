package cn.edu.nju.charlesfeng.util.exceptions.pay;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 支付宝支付时余额不足
 * @author Shenmiu
 */
public class AlipayBalanceNotAdequateException extends MyException {
    public AlipayBalanceNotAdequateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
