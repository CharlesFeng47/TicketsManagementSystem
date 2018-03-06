package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ScheduleItemTypeFormatter implements Formatter<ScheduleItemType> {

    @Override
    public ScheduleItemType parse(String s, Locale locale) throws ParseException {
        System.out.println("------- FORMAT ScheduleItemType -------");
        ScheduleItemType result = ScheduleItemType.getEnum(s);
        assert result != null;
        return result;
    }

    @Override
    public String print(ScheduleItemType scheduleItemType, Locale locale) {
        return scheduleItemType.toString();
    }
}
