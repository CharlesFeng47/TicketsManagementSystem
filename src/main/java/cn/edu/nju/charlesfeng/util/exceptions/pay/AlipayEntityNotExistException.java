package cn.edu.nju.charlesfeng.util.exceptions.pay;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 支付宝账号不存在
 * @author Shenmiu
 */
public class AlipayEntityNotExistException extends MyException {
    public AlipayEntityNotExistException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
