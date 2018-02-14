package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 场馆中的一项活动／计划
 */
public class ScheduleItem {

    /**
     * 此活动开始的时间
     */
    private LocalDateTime startDateTime;

    /**
     * 此活动的类型
     */
    private ScheduleItemType type;

    /**
     * 此活动中每种座位与其对应价格的映射
     */
    private Map<SeatInfo, Double> seatPrices;

    /**
     * 此活动的简单描述
     */
    private String description;

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public ScheduleItemType getType() {
        return type;
    }

    public Map<SeatInfo, Double> getSeatPrices() {
        return seatPrices;
    }

    public String getDescription() {
        return description;
    }
}
