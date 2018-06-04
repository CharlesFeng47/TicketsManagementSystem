package cn.edu.nju.charlesfeng.filter;

import cn.edu.nju.charlesfeng.model.Coupon;
import cn.edu.nju.charlesfeng.model.Member;

import java.util.List;

/**
 * 前端场馆获取到的会员信息
 */
public class ContentMemberOfSpot {

    /**
     * 会员等级
     */
    private int level;

    /**
     * 此会员的优惠券
     */
    private List<Coupon> coupons;

    public ContentMemberOfSpot(Member member) {
        this.level = member.getLevel();
        this.coupons = member.getCoupons();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
