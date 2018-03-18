package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

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
     * 订单中各类型座位及其数量的map
     * 若订单类型为NOT_CHOOSE_SEATS，此项则只有一项entry
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "seat_id")
    @Column(name = "seat_num", nullable = false)
    @CollectionTable(
            name = "order_seat_num",
            joinColumns = {
                    @JoinColumn(name = "order_id")
            }
    )
    private Map<SeatInfo, Integer> seats;

    /**
     * 此订单中包含的座位具体情况 json 串
     * 若订单类型为NOT_CHOOSE_SEATS，此项可为空
     */
    @Column(name = "ordered_seats_json")
    private String orderedSeatsJson;

    /**
     * 订单状态
     */
    @Column(name = "state", nullable = false)
    private OrderState orderState;

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

    public Map<SeatInfo, Integer> getSeats() {
        return seats;
    }

    public void setSeats(Map<SeatInfo, Integer> seats) {
        this.seats = seats;
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
