package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 系统中场馆实体
 */
@Entity
@Table(name = "spot")
public class Spot extends User implements Serializable {

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
    private Set<SeatInfo> seatInfos;

    public Spot() {
    }

    public Spot(String id, String pwd, boolean examined, String site, Set<SeatInfo> seatInfos) {
        super(id, pwd);
        this.examined = examined;
        this.site = site;
        this.seatInfos = seatInfos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("Spot: examined-").append(examined).append(" ").append(site).append("\n");
        for (SeatInfo seatInfo : seatInfos) {
            builder.append(seatInfo.toString()).append(" ");
        }
        return builder.toString();
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

    public Set<SeatInfo> getSeatInfos() {
        return seatInfos;
    }

    public void setSeatInfos(Set<SeatInfo> seatInfos) {
        this.seatInfos = seatInfos;
    }
}
