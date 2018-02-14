package cn.edu.nju.charlesfeng.dao;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;

/**
 * 数据层对用户的服务
 */
public interface UserDao {

    /**
     * @param id   要查看的用户ID
     * @param type 要查看的用户类型
     * @return 用户详情
     */
    User getUser(String id, UserType type) throws UserNotExistException;
}
