package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Spot;

/**
 * 未经审批的场馆申请
 */
public class UnexaminedSpot {

    /**
     * 此场馆的ID
     */
    private String id;

    /**
     * 此场馆的名字
     */
    private String spotName;

    /**
     * 此场馆的地点
     */
    private String site;

    public UnexaminedSpot(Spot spot) {
        this.id = spot.getId();
        this.spotName = spot.getSpotName();
        this.site = spot.getSite();
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
