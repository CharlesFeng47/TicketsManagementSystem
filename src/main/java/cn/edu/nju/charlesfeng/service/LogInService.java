//package cn.edu.nju.charlesfeng.service;
//
//import cn.edu.nju.charlesfeng.model.Member;
//import cn.edu.nju.charlesfeng.model.SeatInfo;
//import cn.edu.nju.charlesfeng.model.Spot;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.util.enums.UserType;
//import cn.edu.nju.charlesfeng.util.exceptions.*;
//
//import java.util.List;
//
///**
// * 系统中会员、场馆、经理的登录服务
// */
//public interface LogInService {
//
//    /**
//     * @param id    欲登录的会员ID
//     * @param pwd   欲登录的会员密码
//     * @param email 欲登录的会员邮箱
//     * @return 【会员】是否成功注册，成功则返回此会员实体
//     */
//    Member registerMember(String id, String pwd, String email) throws UserNotExistException, UserHasBeenSignUpException, InteriorWrongException;
//
//    /**
//     * @param pwd              场馆使用的密码
//     * @param spotName         此场馆的名称
//     * @param site             此场馆的地点
//     * @param alipayId         此场馆的结算支付宝账户
//     * @param seatInfos        此场馆的座位信息
//     * @param seatsMapJson     此场馆中的座位表JSON
//     * @param curSeatTypeCount 此场馆中的座位类型数量
//     * @return 【场馆】是否成功注册，成功则返回此场馆实体
//     */
//    Spot registerSpot(String pwd, String spotName, String site, String alipayId,
//                      List<SeatInfo> seatInfos, String seatsMapJson, int curSeatTypeCount) throws UserNotExistException, AlipayEntityNotExistException;
//
//    /**
//     * 【所有用户】的登录
//     *
//     * @param id       欲登录的用户ID
//     * @param pwd      欲登录的用户密码
//     * @param userType 欲登录的用户类型
//     * @return 登录结果，成功则返回此用户实体
//     */
//    User logIn(String id, String pwd, UserType userType) throws UserNotExistException, WrongPwdException;
//
//}
