package cn.edu.nju.charlesfeng.util;

import java.time.LocalDateTime;

public class IdGenerator {

    /**
     * @return 产生座位信息的唯一ID标志符
     */
    public static String generateSeatId() {
        return "s" + generateId();

    }

    private static String generateId() {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(now.getMonthValue()).append(now.getDayOfMonth());
        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
        return sb.toString();
    }
}
