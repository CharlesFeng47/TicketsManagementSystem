package cn.edu.nju.charlesfeng.model;

/**
 * 场馆中的座位表示
 */
public class Seat {

    /**
     * 此座位ID（行_列）
     */
    private String id;

    /**
     * 此座位名称
     */
    private String seatName;

    /**
     * 此座位价格
     */
    private double price;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
