package cn.edu.nju.charlesfeng.model;

/**
 * 场馆中的座位信息
 */
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

    public String getSeatName() {
        return seatName;
    }

    public int getNum() {
        return num;
    }
}
