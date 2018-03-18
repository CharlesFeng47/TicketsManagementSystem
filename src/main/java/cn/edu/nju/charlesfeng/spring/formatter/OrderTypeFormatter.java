package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.util.enums.OrderType;
import com.alibaba.fastjson.JSON;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class OrderTypeFormatter implements Formatter<OrderType> {

    @Override
    public OrderType parse(String s, Locale locale) throws ParseException {
        System.out.println("--------------FORMAT OrderType--------------");
        System.out.println(s);
        return OrderType.valueOf(s.toUpperCase());
    }

    @Override
    public String print(OrderType orderType, Locale locale) {
        return JSON.toJSONString(orderType);
    }
}
