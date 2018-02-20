package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.OrderType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Entity
@Table(name = "`order`")
public class Order {

    /**
     * 订单标志符ID
     */
    @Id
    private int id;

    /**
     * 订单的类型
     */
    @Column(name = "type", nullable = false)
    private OrderType orderType;

    /**
     * 订单的下达时间
     */
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    /**
     * 订单价格
     */
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    /**
     * 此订单中包含的座位具体情况 json 串
     */
    @Column(name = "seats_json")
    private String seatsJson;

    /**
     * 已经支付
     */
    @Column(name = "has_payed", nullable = false)
    private boolean hasPayed;

    /**
     * 已经退款
     */
    @Column(name = "has_refunded", nullable = false)
    private boolean hasRefunded;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSeatsJson() {
        return seatsJson;
    }

    public void setSeatsJson(String seatsJson) {
        this.seatsJson = seatsJson;
    }

    public boolean isHasPayed() {
        return hasPayed;
    }

    public void setHasPayed(boolean hasPayed) {
        this.hasPayed = hasPayed;
    }

    public boolean isHasRefunded() {
        return hasRefunded;
    }

    public void setHasRefunded(boolean hasRefunded) {
        this.hasRefunded = hasRefunded;
    }
}
