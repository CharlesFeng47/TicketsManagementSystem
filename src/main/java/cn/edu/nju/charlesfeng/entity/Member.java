package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import java.io.Serializable;

/**
 * 系统中会员实体
 */
public class Member extends User implements Serializable {

    /**
     * 会员等级
     */
    private int level;

    /**
     * 会员是否已经被激活
     */
    private boolean activated;

    /**
     * 会员是否已经被注销
     */
    private boolean invalidated;

    public Member(String id, String pwd, int level, boolean activated, boolean invalidated) {
        super(id, pwd);
        this.level = level;
        this.activated = activated;
        this.invalidated = invalidated;
    }

    public int getLevel() {
        return level;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isInvalidated() {
        return invalidated;
    }
}
