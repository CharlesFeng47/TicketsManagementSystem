package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.AlipayAccount;
import cn.edu.nju.charlesfeng.util.exceptions.AlipayBalanceNotAdequateException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;

/**
 * 模拟支付宝操作
 */
public interface AlipayService {

    /**
     * 转账
     *
     * @param from_account 资金转出的账户
     * @param password     资金转出的账户密码
     * @param to_account   资金转入的账户
     * @param amount       金额
     * @return boolean
     * @throws UserNotExistException 该银行账户不存在
     * @throws WrongPwdException 密码不正确
     * @throws AlipayBalanceNotAdequateException 余额不足
     */
    boolean transferAccounts(String from_account, String password, String to_account, double amount) throws UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException;

    /**
     * 获取用户的支付宝账户
     *
     * @param userID 用户ID
     * @return AlipayAccount
     */
    AlipayAccount getUserAccount(String userID);
}
