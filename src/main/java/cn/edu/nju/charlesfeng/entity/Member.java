package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统中会员实体
 */
@Entity
@Table(name = "member")
public class Member extends User implements Serializable {

    /**
     * 会员邮箱
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 会员等级
     */
    @Column(name = "level", nullable = false)
    private int level;

    /**
     * 会员是否已经被激活
     */
    @Column(name = "activated", nullable = false)
    private boolean activated;

    /**
     * 会员是否已经被注销
     */
    @Column(name = "invalidated", nullable = false)
    private boolean invalidated;

    public Member(String id, String pwd, String email, int level, boolean activated, boolean invalidated) {
        super(id, pwd);
        this.email = email;
        this.level = level;
        this.activated = activated;
        this.invalidated = invalidated;
    }

    public Member(String id, String pwd, String email) {
        super(id, pwd);
        this.email = email;
        this.level = 1;
        this.activated = false;
        this.invalidated = false;
    }

    public Member() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    public void setInvalidated(boolean invalidated) {
        this.invalidated = invalidated;
    }
}
