package cn.edu.nju.charlesfeng.spring.formatter;

import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ScheduleItemTypeFormatter implements Formatter<ProgramType> {

    @Override
    public ProgramType parse(String s, Locale locale) throws ParseException {
        System.out.println("------- FORMAT ProgramType -------");
        ProgramType result = ProgramType.getEnum(s);
        assert result != null;
        return result;
    }

    @Override
    public String print(ProgramType programType, Locale locale) {
        return programType.toString();
    }
}
