package cn.edu.nju.charlesfeng.model;

import java.util.Set;

/**
 * 场馆用户的场馆信息
 */
public class SpotInfo {

    /**
     * 此场馆的地点
     */
    private String site;

    /**
     * 此场馆的座位信息
     */
    private Set<SeatInfo> seatInfos;

    public SpotInfo(String site, Set<SeatInfo> seatInfos) {
        this.site = site;
        this.seatInfos = seatInfos;
    }

    public String getSite() {
        return site;
    }

    public Set<SeatInfo> getSeatInfos() {
        return seatInfos;
    }
}
