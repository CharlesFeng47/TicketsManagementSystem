package cn.edu.nju.charlesfeng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 对支付宝的模拟
 */
@Entity
@Table(name = "alipay_mock")
public class AlipayAccount implements Serializable {

    /**
     * 账户ID
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
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
