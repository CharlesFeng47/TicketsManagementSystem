package cn.edu.nju.charlesfeng.util.helper;

import java.time.*;
import java.util.Date;

/**
 * @author Dong
 */
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
        ZoneId zoneidResult = ZoneId.systemDefault();
        return instant.atZone(zoneidResult).toLocalDateTime();
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
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = LocalTime.of(hour, minute, second);
        LocalDateTime result = LocalDateTime.of(localDate, localTime);
        int value = Integer.parseInt(String.valueOf(localDateTime.getNano()).substring(0, 1));
        if (value >= 5) {
            result = result.plusSeconds(1);
        }
        return result;
    }

//    public static void main(String[] args) {
//        System.out.println(TimeHelper.getLong(LocalDateTime.of(2018,8,5, 19,0,0)));
//    }
}
