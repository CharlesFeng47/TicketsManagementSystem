package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;

/**
 * 系统中会员、场馆的服务
 */
public interface UserService {

    /**
     * TODO 参数未定
     * 【会员】通过邮箱验证会员，验证后才可登录
     *
     * @return 邮箱验证结果
     */
    boolean activateByMail();

    /**
     * 【会员】会员注销、取消资格
     *
     * @param mid 欲注销，使会员资格被取消的会员ID
     * @return 注销结果，成果则true
     */
    boolean invalidate(String mid) throws UserNotExistException;

    /**
     * @param user     欲修改用户信息的用户实体
     * @param userType 欲修改用户信息的用户类型
     * @return 修改结果，成果则true
     */
    boolean modifyUser(User user, UserType userType);

    /**
     * @param id   要查看的用户ID
     * @param type 要查看的用户类型
     * @return 用户详情
     */
    User getUser(String id, UserType type) throws UserNotExistException;
}
