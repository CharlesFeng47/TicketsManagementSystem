package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.entity.Coupon;
import com.alibaba.fastjson.JSON;
import org.springframework.format.Formatter;

import java.util.Locale;

public class CouponFormatter implements Formatter<Coupon> {

    @Override
    public Coupon parse(String s, Locale locale) {
        System.out.println("--------------FORMAT Coupon--------------");
        System.out.println(s);
        if (s.equals("null")) {
            Coupon result = new Coupon();
            result.setId(-1);
            return result;
        }
        return JSON.parseObject(s, Coupon.class);
    }

    @Override
    public String print(Coupon coupon, Locale locale) {
        return JSON.toJSONString(coupon);
    }
}
