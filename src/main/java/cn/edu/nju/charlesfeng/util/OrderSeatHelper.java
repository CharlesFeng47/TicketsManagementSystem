package cn.edu.nju.charlesfeng.util;

import cn.edu.nju.charlesfeng.model.Seat;

import java.util.LinkedList;
import java.util.List;

public class OrderSeatHelper {

    /**
     * 获得座位的ID
     */
    public static List<String> getSeatListIds(List<Seat> seats) {
        List<String> ids = new LinkedList<>();
        for (Seat seat : seats) {
            ids.add(seat.getId());
        }
        return ids;
    }

    /**
     * 替换指定位置的座位为大写（座位锁定）
     */
    public static String orderSpecificSeat(String str, int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, pos));
        sb.append((char) (str.charAt(pos) - 0x20));
        if (pos < str.length() - 1) sb.append(str.substring(pos + 1));
        return sb.toString();
    }

    /**
     * 替换指定位置的座位为小写（座位释放）
     */
    public static String releaseSpecificSeat(String str, int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, pos));
        sb.append((char) (str.charAt(pos) + 0x20));
        if (pos < str.length() - 1) sb.append(str.substring(pos + 1));
        return sb.toString();
    }
}
