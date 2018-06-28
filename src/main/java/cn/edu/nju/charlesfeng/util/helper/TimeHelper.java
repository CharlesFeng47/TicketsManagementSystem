package cn.edu.nju.charlesfeng.util.helper;

import java.time.*;
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

    /**
     * 获取里那个时间的时间间隔，单位分
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 间隔
     */
    public static int getDurationMinute(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return ((Long) duration.toMinutes()).intValue();
    }

    /**
     * 标准化时间，去除纳秒
     *
     * @param localDateTime 标准化前的时间
     * @return 时间
     */
    public static LocalDateTime standardTime(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()));
    }
}
