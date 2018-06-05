//package cn.edu.nju.charlesfeng.spring.formatter;
//
//import cn.edu.nju.charlesfeng.model.NotChoseSeats;
//import com.alibaba.fastjson.JSON;
//import org.springframework.format.Formatter;
//
//import java.util.Locale;
//
//public class ContentNotChoseSeatsFormatter implements Formatter<NotChoseSeats> {
//
//    @Override
//    public NotChoseSeats parse(String s, Locale locale) {
//        System.out.println("--------------FORMAT NotChoseSeats--------------");
//        System.out.println(s);
//        return JSON.parseObject(s, NotChoseSeats.class);
//    }
//
//    @Override
//    public String print(NotChoseSeats notChoseSeats, Locale locale) {
//        return JSON.toJSONString(notChoseSeats);
//    }
//}
