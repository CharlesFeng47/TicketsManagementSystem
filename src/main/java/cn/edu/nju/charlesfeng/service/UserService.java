//package cn.edu.nju.charlesfeng.service;
//
//import cn.edu.nju.charlesfeng.model.Coupon;
//import cn.edu.nju.charlesfeng.model.Member;
//import cn.edu.nju.charlesfeng.model.SeatInfo;
//import cn.edu.nju.charlesfeng.model.Spot;
//import cn.edu.nju.charlesfeng.filter.ContentMemberOfSpot;
//import cn.edu.nju.charlesfeng.filter.UnexaminedSpot;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.util.enums.UserType;
//import cn.edu.nju.charlesfeng.util.exceptions.AlipayEntityNotExistException;
//import cn.edu.nju.charlesfeng.util.exceptions.MemberActiveUrlExpiredException;
//import cn.edu.nju.charlesfeng.util.exceptions.MemberConvertCouponCreditNotEnoughException;
//import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
//
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//import java.util.Map;
//
///**
// * 系统中会员、场馆的服务
// */
//public interface UserService {
//
//    /**
//     * 【会员】通过邮箱验证会员，验证后才可登录
//     *
//     * @param activeUrl 验证的连接参数
//     * @return 邮箱验证结果
//     */
//    boolean activateByMail(String activeUrl) throws UnsupportedEncodingException, UserNotExistException, MemberActiveUrlExpiredException;
//
//    /**
//     * 【会员】会员注销、取消资格
//     *
//     * @param member 欲注销，使会员资格被取消的会员
//     * @return 注销结果，成果则true
//     */
//    boolean invalidate(Member member);
//
//    /**
//     * @param curMember 欲修改会员的实体
//     * @param pwd       欲修改会员的密码
//     * @return 修改结果，成果则true
//     */
//    boolean modifyMember(Member curMember, String pwd);
//
//    /**
//     * @param curSpot          要修改的场馆实体
//     * @param pwd              场馆使用的密码
//     * @param spotName         此场馆的名称
//     * @param site             此场馆的地点
//     * @param alipayId         此场馆的结算支付宝账户
//     * @param seatInfos        此场馆的座位信息
//     * @param seatsMapJson     此场馆中的座位表JSON
//     * @param curSeatTypeCount 此场馆中的座位类型数量
//     * @return 【场馆】是否成功修改，成功则返回此场馆实体
//     */
//    boolean modifySpot(Spot curSpot, String pwd, String spotName, String site, String alipayId,
//                       List<SeatInfo> seatInfos, String seatsMapJson, int curSeatTypeCount) throws AlipayEntityNotExistException;
//
//    /**
//     * @param id   要查看的用户ID
//     * @param type 要查看的用户类型
//     * @return 用户详情
//     */
//    User getUser(String id, UserType type) throws UserNotExistException;
//
//    /**
//     * @return 所有场馆ID与其对应实体的映射
//     */
//    Map<String, Spot> getAllSpotIdMap() throws UserNotExistException;
//
//    /**
//     * @param member 要兑换优惠券的用户
//     * @param coupon 要兑换的优惠券
//     * @return 兑换结果，成功则返回成功更新后的会员实体
//     */
//    Member memberConvertCoupon(Member member, Coupon coupon) throws MemberConvertCouponCreditNotEnoughException;
//
//    /**
//     * @param mid 欲获取的会员ID
//     * @return 场馆能获取到的用户信息
//     */
//    ContentMemberOfSpot getMemberOfSpot(String mid) throws UserNotExistException;
//
//    /**
//     * @return 获取所有未被审核的场馆信息
//     */
//    List<UnexaminedSpot> getAllUnexaminedSpots() throws UserNotExistException;
//
//    /**
//     * @param sid 审核通过的场馆ID
//     * @return 审核结果，成功则true
//     */
//    boolean examineSpot(String sid) throws UserNotExistException;
//}
