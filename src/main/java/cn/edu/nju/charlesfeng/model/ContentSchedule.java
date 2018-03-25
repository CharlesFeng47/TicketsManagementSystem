package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
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
     * 此活动中场馆的座位名称
     */
    private List<String> seatNames;

    /**
     * 此活动中每种座位与其对应价格的映射
     */
    private List<SeatInfo> allSeats;

    /**
     * 此活动中与每种座位对应的价格
     */
    private List<Double> allPrices;

    /**
     * 此活动的简单描述
     */
    private String description;

    /**
     * 此场馆中座位的具体情况 json 串
     * 在前端用于生成座位图
     */
    private String allSeatsJson;

    /**
     * 此次计划此场馆中座位的剩余情况 json 串
     * 在前端用于计算座位类型的剩余数量
     */
    private String remainSeatsJson;

    /**
     * 此次计划此场馆中已经被预定了的座位ID列表 json 串
     * 在前端用于设置这些座位不可预定
     */
    private String bookedSeatsIdJson;

    public ContentSchedule(Schedule schedule) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.spotName = schedule.getSpot().getSpotName();
        this.startDateTime = schedule.getStartDateTime().toString().replace('T', ' ');
        this.type = schedule.getType().toString();
        this.description = schedule.getDescription();
        this.allSeatsJson = schedule.getSpot().getAllSeatsJson();
        this.remainSeatsJson = schedule.getRemainSeatsJson();
        this.bookedSeatsIdJson = schedule.getBookedSeatsIdJson();

        this.seatNames = new LinkedList<>();
        for (SeatInfo seatInfo : schedule.getSpot().getSeatInfos()) {
            this.seatNames.add(seatInfo.getSeatName());
        }

        // 对计划中的座位按价格降序排序
        Map<SeatInfo, Double> priceMap = JSON.parseObject(schedule.getSeatInfoPricesJson(), new TypeReference<HashMap<SeatInfo, Double>>() {
        });

        List<Map.Entry<SeatInfo, Double>> relativeList = new ArrayList<>(priceMap.entrySet());
//        relativeList.sort(new SeatPriceMapComparator());

        allSeats = new LinkedList<>();
        allPrices = new LinkedList<>();
        for (Map.Entry<SeatInfo, Double> entry : relativeList) {
            allSeats.add(entry.getKey());
            allPrices.add(entry.getValue());
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

    public List<String> getSeatNames() {
        return seatNames;
    }

    public void setSeatNames(List<String> seatNames) {
        this.seatNames = seatNames;
    }

    public List<SeatInfo> getAllSeats() {
        return allSeats;
    }

    public void setAllSeats(List<SeatInfo> allSeats) {
        this.allSeats = allSeats;
    }

    public List<Double> getAllPrices() {
        return allPrices;
    }

    public void setAllPrices(List<Double> allPrices) {
        this.allPrices = allPrices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAllSeatsJson() {
        return allSeatsJson;
    }

    public void setAllSeatsJson(String allSeatsJson) {
        this.allSeatsJson = allSeatsJson;
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
}
