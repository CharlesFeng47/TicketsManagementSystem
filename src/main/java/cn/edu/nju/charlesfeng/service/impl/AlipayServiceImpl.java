package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.AlipayAccount;
import cn.edu.nju.charlesfeng.repository.AlipayRepository;
import cn.edu.nju.charlesfeng.service.AlipayService;
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.member.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.member.WrongPwdException;
import cn.edu.nju.charlesfeng.util.exceptions.pay.AlipayBalanceNotAdequateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AlipayServiceImpl implements AlipayService {

    private final AlipayRepository alipayRepository;

    @Autowired
    public AlipayServiceImpl(AlipayRepository alipayRepository) {
        this.alipayRepository = alipayRepository;
    }

    /**
     * 转账
     *
     * @param from_account 资金转出的账户
     * @param password     资金转出的账户密码
     * @param to_account   资金转入的账户
     * @param amount       金额
     * @return boolean
     * @throws UserNotExistException             该银行账户不存在
     * @throws WrongPwdException                 密码不正确
     * @throws AlipayBalanceNotAdequateException 余额不足
     */
    @Override
    //@Transactional
    public void transferAccounts(String from_account, String password, String to_account, double amount) throws UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException {
        AlipayAccount from_externalBalance = null;
        try {
            from_externalBalance = alipayRepository.getOne(from_account);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new UserNotExistException(ExceptionCode.USER_NOT_EXIST, from_account);
        }


        if (!password.equals(from_externalBalance.getPwd())) {
            throw new WrongPwdException(ExceptionCode.USER_PWD_WRONG);
        }

        if (Double.doubleToLongBits(amount) > Double.doubleToLongBits(from_externalBalance.getBalance())) {
            throw new AlipayBalanceNotAdequateException(ExceptionCode.PAY_BALANCE_NOT_ADEQUATE);
        }

        AlipayAccount to_externalBalance = null;
        try {
            to_externalBalance = alipayRepository.getOne(to_account);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new UserNotExistException(ExceptionCode.USER_NOT_EXIST, to_account);
        }

        from_externalBalance.setBalance(from_externalBalance.getBalance() - amount);
        to_externalBalance.setBalance(to_externalBalance.getBalance() + amount);
        alipayRepository.save(from_externalBalance);
        alipayRepository.save(to_externalBalance);
    }

    /**
     * 获取用户的支付宝账户
     *
     * @param userID 用户ID
     * @return AlipayAccount
     */
    @Override
    public AlipayAccount getUserAccount(String userID) {
        return alipayRepository.getOne(userID);
    }
}
