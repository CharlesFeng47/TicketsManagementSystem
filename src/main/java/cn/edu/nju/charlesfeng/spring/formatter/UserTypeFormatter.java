package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.util.enums.UserType;
import com.alibaba.fastjson.JSON;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class UserTypeFormatter implements Formatter<UserType> {

    @Override
    public UserType parse(String text, Locale locale) throws ParseException {
        System.out.println("--------------FORMAT UserType--------------");
        System.out.println(text);
        return UserType.valueOf(text.toUpperCase());
    }

    @Override
    public String print(UserType object, Locale locale) {
        return JSON.toJSONString(object);
    }
}
