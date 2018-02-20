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

    /**
     * @param user 要保存的用户实体
     * @param type 要保存的用户类型
     * @return 成功保存后的此实体对象主键
     */
    String saveUser(User user, UserType type);

    /**
     * @param user 要修改的用户实体
     * @param type 要修改的用户类型
     * @return 修改结果，成功则true
     */
    boolean updateUser(User user, UserType type);
}
