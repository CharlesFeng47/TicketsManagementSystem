package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Spot;
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

    /**
     * 场馆是否已经被审核
     */
    private boolean examined;

    public ContentUser(User user, UserType userType) {
        this.user = user;
        role = new String[1];
        role[0] = userType.toString();

        if (userType == UserType.SPOT) this.examined = ((Spot) user).isExamined();
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

    public boolean isExamined() {
        return examined;
    }

    public void setExamined(boolean examined) {
        this.examined = examined;
    }
}
