package cn.edu.nju.charlesfeng.filter;

import cn.edu.nju.charlesfeng.model.NotChoseSeats;

/**
 * 不选座购票时的座位类型信息，供前端使用。（移除Order，避免JSON转换）
 */
public class ContentNotChoseSeats {

    /**
     * 此座位情况的标志符
     */
    private int id;

    /**
     * 此座位情况所在的座位类型名称
     */
    private String seatName;

    /**
     * 此座位情况的购买数量
     */
    private int num;

    /**
     * 此座位情况的价格
     */
    private double price;

    public ContentNotChoseSeats(NotChoseSeats notChoseSeats) {
        if (notChoseSeats != null) {
            this.id = notChoseSeats.getId();
            this.seatName = notChoseSeats.getSeatName();
            this.num = notChoseSeats.getNum();
            this.price = notChoseSeats.getPrice();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
