package cn.edu.nju.charlesfeng.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 系统中的所有合法用户实体的抽象父类
 */
@MappedSuperclass
public abstract class User {

    /**
     * 用户ID
     */
    @Id
    private String id;

    /**
     * 用户密码
     */
    private String pwd;

    public User(String id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

    protected User() {
    }

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }
}
