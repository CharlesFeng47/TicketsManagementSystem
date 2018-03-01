package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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
    private int id;

    /**
     * 此活动的名字
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 此活动的举行地点ID
     */
    @Column(name = "spot_id", nullable = false)
    private String spotId;

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
     * 此活动中每种座位与其对应价格的映射
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "seat_id")
    @Column(name = "price", nullable = false)
    @CollectionTable(
            name = "schedule_seat_price",
            joinColumns = {
                    @JoinColumn(name = "schedule_id")
            }

    )
    private Map<SeatInfo, Double> seatPrices;

    /**
     * 此活动的简单描述
     */
    @Column(name = "description", nullable = false)
    private String description;

    public Schedule() {
    }

    public Schedule(String name, String spotId, LocalDateTime startDateTime, ScheduleItemType type, Map<SeatInfo, Double> seatPrices, String description) {
        this.name = name;
        this.spotId = spotId;
        this.startDateTime = startDateTime;
        this.type = type;
        this.seatPrices = seatPrices;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
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

    public Map<SeatInfo, Double> getSeatPrices() {
        return seatPrices;
    }

    public void setSeatPrices(Map<SeatInfo, Double> seatPrices) {
        this.seatPrices = seatPrices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
