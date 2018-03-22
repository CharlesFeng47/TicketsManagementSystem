package cn.edu.nju.charlesfeng.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 对支付宝的模拟
 */
@Entity
@Table(name = "alipay_mock")
public class AlipayEntity {

    /**
     * 账户ID
     */
    @Id
    private String id;

    /**
     * 账户密码
     */
    @Column(name = "pwd", nullable = false)
    private String pwd;

    /**
     * 账户余额
     */
    @Column(name = "balance", nullable = false)
    private double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
