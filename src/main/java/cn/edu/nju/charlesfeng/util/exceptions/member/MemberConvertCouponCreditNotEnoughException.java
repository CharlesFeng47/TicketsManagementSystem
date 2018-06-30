package cn.edu.nju.charlesfeng.util.exceptions.member;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;

/**
 * 会员兑换优惠券时剩余积分不足导致的一场
 */
public class MemberConvertCouponCreditNotEnoughException extends MyException {
    public MemberConvertCouponCreditNotEnoughException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
