package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * 前端非场馆本人请求场馆数据时可以查看到的场馆数据
 */
public class ContentSpot {

    /**
     * 场馆ID
     */
    private String id;

    /**
     * 此场馆的名字
     */
    @Column(name = "spot_name", nullable = false)
    private String spotName;

    /**
     * 此场馆的座位信息
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<String> seatNames;

    /**
     * 此场馆中座位的具体情况 json 串
     */
    @Column(name = "all_seats_json", nullable = false)
    private String allSeatsJson;

    /**
     * 此场馆座位图中的座位类型数量
     */
    @Column(name = "seat_type_count", nullable = false)
    private int curSeatTypeCount;

    public ContentSpot(Spot spot) {
        this.id = spot.getId();
        this.spotName = spot.getSpotName();
        this.allSeatsJson = spot.getAllSeatsJson();
        this.curSeatTypeCount = spot.getCurSeatTypeCount();

        this.seatNames = new LinkedList<>();
        for (SeatInfo seatInfo : spot.getSeatInfos()) {
            this.seatNames.add(seatInfo.getSeatName());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public List<String> getSeatNames() {
        return seatNames;
    }

    public void setSeatNames(List<String> seatNames) {
        this.seatNames = seatNames;
    }

    public String getAllSeatsJson() {
        return allSeatsJson;
    }

    public void setAllSeatsJson(String allSeatsJson) {
        this.allSeatsJson = allSeatsJson;
    }

    public int getCurSeatTypeCount() {
        return curSeatTypeCount;
    }

    public void setCurSeatTypeCount(int curSeatTypeCount) {
        this.curSeatTypeCount = curSeatTypeCount;
    }
}
