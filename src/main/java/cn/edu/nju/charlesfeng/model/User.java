package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.UserType;

/**
 * 系统中的所有合法用户实体的抽象父类
 */
public abstract class User {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 用户类型
     */
    private UserType userType;

    public User(String id, String pwd, UserType userType) {
        this.id = id;
        this.pwd = pwd;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public UserType getUserType() {
        return userType;
    }
}
