package cn.edu.nju.charlesfeng.model;

/**
 * 选座购票时的单个座位信息情况
 */
public class ChoseSeat {

    /**
     * 此座位的ID（行_列）
     */
    private String id;

    /**
     * 此座位所在的座位类型名称
     */
    private String seatName;

    /**
     * 此座位的价格
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
