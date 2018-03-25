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
}
