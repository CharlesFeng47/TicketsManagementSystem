package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.util.comparators.SeatPriceMapComparator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.*;

/**
 * 因为前端没找到合适的方法转化Map，所以方便前端处理
 */
public class ContentSchedule {

    /**
     * 此活动的标志符ID
     */
    private String id;

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

    /**
     * 此活动中每种座位与其对应价格的映射
     */
    private List<SeatInfo> all_seats;

    /**
     * 此活动中与每种座位对应的价格
     */
    private List<Double> all_prices;

    /**
     * 此活动的简单描述
     */
    private String description;

    public ContentSchedule(Schedule schedule) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.spotName = schedule.getSpot().getSpotName();
        this.startDateTime = schedule.getStartDateTime().toString().replace('T', ' ');
        this.type = schedule.getType().toString();
        this.description = schedule.getDescription();

        // 对计划中的座位按价格降序排序
        Map<SeatInfo, Double> priceMap = JSON.parseObject(schedule.getSeatInfoPricesJson(), new TypeReference<HashMap<SeatInfo, Double>>() {
        });

        List<Map.Entry<SeatInfo, Double>> relativeList = new ArrayList<>(priceMap.entrySet());
        relativeList.sort(new SeatPriceMapComparator());

        all_seats = new LinkedList<>();
        all_prices = new LinkedList<>();
        for (Map.Entry<SeatInfo, Double> entry : relativeList) {
            all_seats.add(entry.getKey());
            all_prices.add(entry.getValue());
        }
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

    public List<SeatInfo> getAll_seats() {
        return all_seats;
    }

    public void setAll_seats(List<SeatInfo> all_seats) {
        this.all_seats = all_seats;
    }

    public List<Double> getAll_prices() {
        return all_prices;
    }

    public void setAll_prices(List<Double> all_prices) {
        this.all_prices = all_prices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
