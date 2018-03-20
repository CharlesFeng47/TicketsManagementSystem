package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.util.enums.OrderWay;
import com.alibaba.fastjson.JSON;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class OrderWayFormatter implements Formatter<OrderWay> {

    @Override
    public OrderWay parse(String s, Locale locale) throws ParseException {
        System.out.println("--------------FORMAT OrderWay--------------");
        System.out.println(s);
        return OrderWay.valueOf(s.toUpperCase());
    }

    @Override
    public String print(OrderWay orderWay, Locale locale) {
        return JSON.toJSONString(orderWay);
    }
}
