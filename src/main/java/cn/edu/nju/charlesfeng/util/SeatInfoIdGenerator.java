package cn.edu.nju.charlesfeng.util;

import java.time.LocalDateTime;

public class SeatInfoIdGenerator {

    /**
     * @return 产生座位信心的唯一ID标志符
     */
    public static String generateSeatId() {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append("s").append(now.getMonthValue()).append(now.getDayOfMonth());
        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
        return sb.toString();
    }
}
