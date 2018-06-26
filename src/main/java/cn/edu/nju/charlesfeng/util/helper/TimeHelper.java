package cn.edu.nju.charlesfeng.util.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeHelper {

    private TimeHelper() {
    }

    /**
     * LocalDateTime -> Long
     *
     * @param localDateTime 时间
     * @return long
     */
    public static long getLong(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date.getTime();
    }

    /**
     * Long -> LocalDateTime
     *
     * @param time 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(long time) {
        Date result = new Date(time);
        Instant instant = result.toInstant();
        ZoneId zoneId_result = ZoneId.systemDefault();
        return instant.atZone(zoneId_result).toLocalDateTime();
    }
}
