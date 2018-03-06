package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 系统中场馆实体
 */
@Entity
@Table(name = "spot")
public class Spot extends User implements Serializable {

    /**
     * 此场馆的名字
     */
    @Column(name = "spot_name", nullable = false)
    private String spotName;

    /**
     * 被审批过的
     * 注册申请/修改场馆信息均需被审批
     */
    @Column(name = "examined", nullable = false)
    private boolean examined;

    /**
     * 此场馆的地点
     */
    @Column(name = "site", nullable = false)
    private String site;

    /**
     * 此场馆的座位信息
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SeatInfo> seatInfos;

    /**
     * 此场馆中座位的具体情况 json 串
     */
    @Column(name = "all_seats_json")
    private String allSeatsJson;

    public Spot() {
    }

    public Spot(String pwd, String spotName, boolean examined, String site, List<SeatInfo> seatInfos, String allSeatsJson) {
        super.setPwd(pwd);
        this.spotName = spotName;
        this.examined = examined;
        this.site = site;
        this.seatInfos = seatInfos;
        this.allSeatsJson = allSeatsJson;
    }

    public Spot(String id, String pwd, String spotName, boolean examined, String site, List<SeatInfo> seatInfos, String allSeatsJson) {
        super(id, pwd);
        this.spotName = spotName;
        this.examined = examined;
        this.site = site;
        this.seatInfos = seatInfos;
        this.allSeatsJson = allSeatsJson;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public boolean isExamined() {
        return examined;
    }

    public void setExamined(boolean examined) {
        this.examined = examined;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<SeatInfo> getSeatInfos() {
        return seatInfos;
    }

    public void setSeatInfos(List<SeatInfo> seatInfos) {
        this.seatInfos = seatInfos;
    }

    public String getAllSeatsJson() {
        return allSeatsJson;
    }

    public void setAllSeatsJson(String allSeatsJson) {
        this.allSeatsJson = allSeatsJson;
    }
}
