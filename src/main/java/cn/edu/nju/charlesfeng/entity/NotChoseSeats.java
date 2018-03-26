package cn.edu.nju.charlesfeng.entity;

import javax.persistence.*;

/**
 * 不选座购票时的座位类型信息
 */
@Entity
@Table(name = "not_chose_seats")
public class NotChoseSeats {

    /**
     * 此不选座购买座位情况的标志符
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 此座位情况所在的座位类型名称
     */
    @Column(name = "name", nullable = false)
    private String seatName;

    /**
     * 此座位情况所在的座位类型表示字符
     */
    @Column(name = "seat_char", nullable = false)
    private char seatChar;

    /**
     * 此座位情况的购买数量
     */
    @Column(name = "num", nullable = false)
    private int num;

    /**
     * 此座位情况的价格
     */
    @Column(name = "price", nullable = false)
    private double price;

    /**
     * 此座位情况关联的订单对象
     */
    @OneToOne(mappedBy = "notChoseSeats")
    private Order order;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
