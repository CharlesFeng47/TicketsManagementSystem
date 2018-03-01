package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.UserType;

/**
 * 前端使用的用户数据
 */
public class ContentUser {

    /**
     * 用户实体
     */
    private User user;

    /**
     * 本例中只有一个role，但是前端需要一个数组
     */
    private String[] role;

    public ContentUser(User user, UserType userType) {
        this.user = user;
        role = new String[1];
        role[0] = userType.toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }
}
