package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.User;

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
     * @param uid 欲注销，使会员资格被取消的会员ID
     * @return 注销结果，成果则true
     */
    boolean invalidate(String uid);

    /**
     * @param user 欲修改用户信息的用户
     * @return 修改结果，成果则true
     */
    boolean modifyUser(User user);

    /**
     * @param id 要查看的用户ID
     * @return 用户详情
     */
    User getUser(String id);
}
