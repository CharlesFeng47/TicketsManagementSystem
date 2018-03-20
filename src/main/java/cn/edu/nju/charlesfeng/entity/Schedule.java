package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    public Schedule() {
    }

    public Schedule(String id, String name, Spot spot, LocalDateTime startDateTime, ScheduleItemType type, String seatInfoPricesJson, String description) {
        this.id = id;
        this.name = name;
        this.spot = spot;
        this.startDateTime = startDateTime;
        this.type = type;
        this.seatInfoPricesJson = seatInfoPricesJson;
        this.description = description;
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
}
