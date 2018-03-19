package cn.edu.nju.charlesfeng.entity;

import javax.persistence.*;

/**
 * 优惠券实体
 */
@Entity
@Table(name = "coupon")
public class Coupon {

    /**
     * 优惠券标志符ID，hibernate自己自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    /**
     * 此优惠券的金额
     */
    @Column(name = "price", nullable = false)
    private double offPrice;

    /**
     * 兑换此优惠券花费的积分
     */
    @Column(name = "credit", nullable = false)
    private double neededCredit;

    /**
     * 此优惠券的描述
     */
    @Column(name = "description", nullable = false)
    private String description;

    public Coupon() {
    }

    public Coupon(double offPrice, double neededCredit, String description) {
        this.offPrice = offPrice;
        this.neededCredit = neededCredit;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getOffPrice() {
        return offPrice;
    }

    public void setOffPrice(double offPrice) {
        this.offPrice = offPrice;
    }

    public double getNeededCredit() {
        return neededCredit;
    }

    public void setNeededCredit(double neededCredit) {
        this.neededCredit = neededCredit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
