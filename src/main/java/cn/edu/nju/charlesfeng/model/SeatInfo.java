package cn.edu.nju.charlesfeng.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 场馆中的座位信息
 */
@Entity
@Table(name = "seat")
public class SeatInfo {

    /**
     * 此座位的ID
     */
    @Id
    private String id;

    /**
     * 此座位的名称
     */
    @Column(name = "seat_name", nullable = false)
    private String seatName;

    /**
     * 此座位的字母表示
     */
    @Column(name = "seat_char", nullable = false)
    private char seatChar;

    /**
     * 此座位的个数
     */
    @Column(name = "seat_num", nullable = false)
    private int num;

    public SeatInfo() {
    }

    public SeatInfo(String id, String seatName, int num) {
        this.id = id;
        this.seatName = seatName;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public char getSeatChar() {
        return seatChar;
    }

    public void setSeatChar(char seatChar) {
        this.seatChar = seatChar;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
