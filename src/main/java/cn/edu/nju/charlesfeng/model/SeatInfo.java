package cn.edu.nju.charlesfeng.model;

import javax.persistence.Entity;

/**
 * 场馆中的座位信息
 */
@Entity
public class SeatInfo {

    /**
     * 此座位的名称
     */
    private String seatName;

    /**
     * 此座位的个数
     */
    private int num;

    public SeatInfo(String seatName, int num) {
        this.seatName = seatName;
        this.num = num;
    }

    @Override
    public String toString() {
        return seatName + ": " + num;
    }

    public String getSeatName() {
        return seatName;
    }

    public int getNum() {
        return num;
    }
}
