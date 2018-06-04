package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
import cn.edu.nju.charlesfeng.util.enums.ScheduleState;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 场馆中的一项活动／计划
 */
@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {

    /**
     * 此活动的标志符ID
     */
    @Id
    private String id;

    /**
     * 此活动的名字
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 此活动的举行地点ID
     */
    @ManyToOne
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    /**
     * 此活动开始的时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startDateTime;

    /**
     * 此活动的类型
     */
    @Column(name = "type", nullable = false)
    private ScheduleItemType type;

    /**
     * 此活动中每种座位与其对应价格的映射 json 串
     * 注：原本的map注解可以正常工作，但是在将场馆spot映射为实体之后，这个map就不行了
     * 尝试改将entry封装为对象转为List，仍失败，时间不足，更换json串实现
     */
    @Column(name = "seat_info_price_json", nullable = false)
    private String seatInfoPricesJson;

    /**
     * 此活动的简单描述
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 此次计划此场馆中座位的剩余情况 json 串（与场馆中 allSeatsJson 的区别在于此 remainSeatsJson 被预定后字符专为大写）
     */
    @Column(name = "remain_seats_json", nullable = false)
    private String remainSeatsJson;

    /**
     * 此次计划此场馆中已经被预定了的座位ID列表 json 串
     */
    @Column(name = "booked_seats_id_json", nullable = false)
    private String bookedSeatsIdJson;

    /**
     * 此计划相关的订单
     */
    @OneToMany(mappedBy = "schedule", targetEntity = Order.class,
            fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Column(name = "orders", nullable = false)
    @Fetch(FetchMode.SUBSELECT)
    private List<Order> orders;

    /**
     * 计划的状态
     */
    @Column(name = "state", nullable = false)
    private ScheduleState state;

    /**
     * 订单下达之后支付款项，暂存到计划中，待结算
     */
    @Column(name = "balance", nullable = false)
    private double balance;

    public Schedule() {
    }

    public Schedule(String id, String name, Spot spot, LocalDateTime startDateTime, ScheduleItemType type, String seatInfoPricesJson, String description, String remainSeatsJson, String bookedSeatsIdJson, List<Order> orders, ScheduleState state, double balance) {
        this.id = id;
        this.name = name;
        this.spot = spot;
        this.startDateTime = startDateTime;
        this.type = type;
        this.seatInfoPricesJson = seatInfoPricesJson;
        this.description = description;
        this.remainSeatsJson = remainSeatsJson;
        this.bookedSeatsIdJson = bookedSeatsIdJson;
        this.orders = orders;
        this.state = state;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ScheduleItemType getType() {
        return type;
    }

    public void setType(ScheduleItemType type) {
        this.type = type;
    }

    public String getSeatInfoPricesJson() {
        return seatInfoPricesJson;
    }

    public void setSeatInfoPricesJson(String seatInfoPricesJson) {
        this.seatInfoPricesJson = seatInfoPricesJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemainSeatsJson() {
        return remainSeatsJson;
    }

    public void setRemainSeatsJson(String remainSeatsJson) {
        this.remainSeatsJson = remainSeatsJson;
    }

    public String getBookedSeatsIdJson() {
        return bookedSeatsIdJson;
    }

    public void setBookedSeatsIdJson(String bookedSeatsIdJson) {
        this.bookedSeatsIdJson = bookedSeatsIdJson;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public ScheduleState getState() {
        return state;
    }

    public void setState(ScheduleState state) {
        this.state = state;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
