package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;

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
    Member registerMember(String id, String pwd) throws UserNotExistException;

    /**
     * @param id        此场馆的ID
     * @param pwd       场馆使用的密码
     * @param site      此场馆的地点
     * @param seatInfos 此场馆的座位信息
     * @param seats     此场馆中每一个具体座位
     * @return 【场馆】是否成功注册，成功则返回此场馆实体
     */
    Spot registerSpot(String id, String pwd, String site, Set<SeatInfo> seatInfos, Set<Seat> seats) throws UserNotExistException;

    /**
     * 【所有用户】的登录
     *
     * @param id       欲登录的用户ID
     * @param pwd      欲登录的用户密码
     * @param userType 欲登录的用户类型
     * @return 登录结果，成功则返回此用户实体
     */
    User logIn(String id, String pwd, UserType userType) throws UserNotExistException, WrongPwdException;

}
