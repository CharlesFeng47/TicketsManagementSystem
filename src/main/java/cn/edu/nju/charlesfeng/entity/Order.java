package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 会员ID
     */
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private Member member;

    /**
     * 订单下达的计划ID
     */
    @ManyToOne
    @JoinColumn(name = "sid", nullable = false)
    private Schedule schedule;

    /**
     * 订单状态
     */
    @Column(name = "state", nullable = false)
    private OrderState orderState;

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
     * 订单总价
     */
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    /**
     * 订单类型为NOT_CHOOSE_SEATS时，选择的座位情况
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ncs_id")
    private NotChoseSeats notChoseSeats;

    /**
     * 此订单中包含的座位具体情况 json 串【因为其信息本身就比较冗余，所以以 json 串形式存储，不再单独映射为表】
     * 若订单类型为NOT_CHOOSE_SEATS，此项可暂为空
     */
    @Column(name = "ordered_seats_json")
    private String orderedSeatsJson;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
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

    public NotChoseSeats getNotChoseSeats() {
        return notChoseSeats;
    }

    public void setNotChoseSeats(NotChoseSeats notChoseSeats) {
        this.notChoseSeats = notChoseSeats;
    }

    public String getOrderedSeatsJson() {
        return orderedSeatsJson;
    }

    public void setOrderedSeatsJson(String orderedSeatsJson) {
        this.orderedSeatsJson = orderedSeatsJson;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
