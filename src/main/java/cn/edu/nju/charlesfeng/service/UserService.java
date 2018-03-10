package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;

import java.util.List;
import java.util.Map;

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
     * @param sid              场馆id
     * @param pwd              场馆使用的密码
     * @param spotName         此场馆的名称
     * @param site             此场馆的地点
     * @param seatInfos        此场馆的座位信息
     * @param seatsMapJson     此场馆中的座位表JSON
     * @param curSeatTypeCount 此场馆中的座位类型数量
     * @return 【场馆】是否成功修改，成功则返回此场馆实体
     */
    boolean modifySpot(String sid, String pwd, String spotName, String site, List<SeatInfo> seatInfos, String seatsMapJson,
                       int curSeatTypeCount) throws UserNotExistException;

    /**
     * @param id   要查看的用户ID
     * @param type 要查看的用户类型
     * @return 用户详情
     */
    User getUser(String id, UserType type) throws UserNotExistException;

    /**
     * @return 所有场馆ID与其对应实体的映射
     */
    Map<String, Spot> getAllSpotIdMap() throws UserNotExistException;
}
