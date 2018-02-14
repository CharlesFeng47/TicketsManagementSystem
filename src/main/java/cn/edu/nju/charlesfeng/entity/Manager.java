package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;

/**
 * 系统中经理实体
 */
public class Manager extends User {

    public Manager(String id, String pwd) {
        super(id, pwd, UserType.MANAGER);
    }
}
