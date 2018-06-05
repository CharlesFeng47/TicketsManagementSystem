package cn.edu.nju.charlesfeng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统的用户实体
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    /**
     *  用户ID，邮箱
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private String email;

    /**
     * 用户登录密码
     */
    @Column(name = "pwd", nullable = false)
    private String password;

    /**
     * 用户名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 头像
     */
    @Column(nullable = false)
    private String portrait;

    /**
     * 是否激活
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean activated;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
