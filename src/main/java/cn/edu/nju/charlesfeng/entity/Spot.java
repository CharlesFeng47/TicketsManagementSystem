package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.SpotInfo;
import cn.edu.nju.charlesfeng.model.User;

import java.io.Serializable;

/**
 * 系统中场馆实体
 */
public class Spot extends User implements Serializable {

    /**
     * 此场馆的场馆信息
     */
    private SpotInfo spotInfo;

    /**
     * 被审批过的
     * 注册申请/修改场馆信息均需被审批
     */
    private boolean examinedd;

    public Spot(String id, String pwd) {
        super(id, pwd);
    }

    public SpotInfo getSpotInfo() {
        return spotInfo;
    }

    public boolean isExaminedd() {
        return examinedd;
    }
}
