package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.Spot;

/**
 * 前端展示的日程简介
 */
public class ContentScheduleBrief {

    /**
     * 此活动的标志符ID
     */
    private int id;

    /**
     * 此活动的名字
     */
    private String name;

    /**
     * 此活动的举行地点ID
     */
    private String spotName;

    /**
     * 此活动开始的时间
     */
    private String startDateTime;

    /**
     * 此活动的类型
     */
    private String type;

    public ContentScheduleBrief(Schedule schedule, Spot relativeSpot) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.spotName = relativeSpot.getSpotName();
        this.startDateTime = schedule.getStartDateTime().toString().replace('T', ' ');
        this.type = schedule.getType().toString();
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

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
