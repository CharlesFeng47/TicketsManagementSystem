package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.SeatInfo;
import cn.edu.nju.charlesfeng.model.User;

import java.io.Serializable;
import java.util.Set;

/**
 * 系统中场馆实体
 */
//@Entity
//@NamedNativeQueries({
//        @NamedNativeQuery(name = "spot_query_all", query = ""),
//        @NamedNativeQuery(name = "spot_query_one", query = ""),
//})
public class Spot extends User implements Serializable {

    /**
     * 被审批过的
     * 注册申请/修改场馆信息均需被审批
     */
    private boolean examined;

    /**
     * 此场馆的地点
     */
    private String site;

    /**
     * 此场馆的座位信息
     */
//    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private Set<SeatInfo> seatInfos;

    public Spot(String id, String pwd) {
        super(id, pwd);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("Spot: examined-").append(examined).append("\n");
        builder.append(site);
        for (SeatInfo seatInfo : seatInfos) {
            builder.append(seatInfo.toString());
        }
        return builder.toString();
    }

    public boolean isExamined() {
        return examined;
    }

    public String getSite() {
        return site;
    }

    public Set<SeatInfo> getSeatInfos() {
        return seatInfos;
    }
}
