package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.exceptions.*;

import java.io.UnsupportedEncodingException;

/**
 * 系统中用户的服务
 */
public interface UserService {

    /**
     * @param user 欲注册用户实体
     * @return 是否成功注册，成功则返回此会员实体
     */
    User register(User user) throws UserHasBeenSignUpException, InteriorWrongException;

    /**
     * 【所有用户】的登录
     *
     * @param id  欲登录的用户ID
     * @param pwd 欲登录的用户密码
     * @return 登录结果，成功则返回此用户实体
     */
    boolean logIn(String id, String pwd) throws UserNotExistException, WrongPwdException, UserNotActivatedException;

    /**
     * 【用户】通过邮箱验证用户，验证后才可登录
     *
     * @param activeUrl 验证的连接参数
     * @return 邮箱验证结果
     */
    boolean activateByMail(String activeUrl) throws UnsupportedEncodingException, UserNotExistException, UserActiveUrlExpiredException;

    /**
     * @param user 欲修改用户的实体
     * @return 修改结果，成果则true
     */
    boolean modifyUser(User user) throws UserNotExistException;

    /**
     * @param id 要查看的用户ID
     * @return 用户详情
     */
    User getUser(String id) throws UserNotExistException;
}
