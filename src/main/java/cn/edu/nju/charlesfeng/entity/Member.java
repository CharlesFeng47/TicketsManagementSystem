package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统中会员实体
 */
@Entity
@Table(name = "member")
public class Member extends User implements Serializable {

    /**
     * 会员邮箱
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 会员等级
     */
    @Column(name = "level", nullable = false)
    private int level;

//    /**
//     * 此会员的优惠券
//     */
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<Coupon> coupons;

    /**
     * 会员是否已经被激活
     */
    @Column(name = "activated", nullable = false)
    private boolean activated;

    /**
     * 会员是否已经被注销
     */
    @Column(name = "invalidated", nullable = false)
    private boolean invalidated;

    public Member(String id, String pwd, String email) {
        super(id, pwd);
        this.email = email;
        this.level = 1;
//        this.coupons = new LinkedList<>();
        this.activated = false;
        this.invalidated = false;
    }

    public Member() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

//    public List<Coupon> getCoupons() {
//        return coupons;
//    }
//
//    public void setCoupons(List<Coupon> coupons) {
//        this.coupons = coupons;
//    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    public void setInvalidated(boolean invalidated) {
        this.invalidated = invalidated;
    }
}
