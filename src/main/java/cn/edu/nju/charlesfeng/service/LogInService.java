package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.SeatInfo;
import cn.edu.nju.charlesfeng.model.User;

import java.util.Set;

/**
 * 系统中会员、场馆、经理的登录服务
 */
public interface LogInService {

    /**
     * @param id  欲登录的会员ID
     * @param pwd 欲登录的会员密码
     * @return 【会员】是否成功注册，成功则返回此会员实体
     */
    Member registerMember(String id, String pwd);

    /**
     * @param pwd       场馆使用的密码
     * @param site      此场馆的地点
     * @param seatInfos 此场馆的座位信息
     * @return 【场馆】是否成功注册，成功则返回此场馆实体
     */
    Spot registerSpot(String pwd, String site, Set<SeatInfo> seatInfos);

    /**
     * 【所有用户】的登录
     *
     * @param id  欲登录的用户ID
     * @param pwd 欲登录的用户密码
     * @return 登录结果，成功则返回此用户实体
     */
    User logIn(String id, String pwd);

}
