package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentMemberOfSpot;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.MemberConvertCouponCreditNotEnoughException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean activateByMail() {
        // TODO mail验证
        return false;
    }

    @Override
    public boolean invalidate(String mid) throws UserNotExistException {
        Member member = (Member) userDao.getUser(mid, UserType.MEMBER);
        member.setInvalidated(true);
        userDao.updateUser(member, UserType.MEMBER);
        return true;
    }

    @Override
    public boolean modifyMember(Member curMember, String pwd) {
        curMember.setPwd(pwd);
        return userDao.updateUser(curMember, UserType.MEMBER);
    }

    @Override
    public boolean modifySpot(Spot curSpot, String pwd, String spotName, String site, List<SeatInfo> seatInfos, String seatsMapJson, int curSeatTypeCount) throws UserNotExistException {
        curSpot.setPwd(pwd);
        curSpot.setSpotName(spotName);
        curSpot.setSite(site);
        curSpot.setAllSeatsJson(seatsMapJson);
        curSpot.setCurSeatTypeCount(curSeatTypeCount);

        // 保留原来的座位信息ID，以不至于原座位数据成为废数据
        String sidBase = curSpot.getSeatInfos().get(0).getId().split("/")[0];
        for (int i = 0; i < seatInfos.size(); i++) {
            SeatInfo curSeatInfo = seatInfos.get(i);
            curSeatInfo.setId(sidBase + "/" + i);
        }
        curSpot.setSeatInfos(seatInfos);

        return userDao.updateUser(curSpot, UserType.SPOT);
    }

    @Override
    public User getUser(String id, UserType type) throws UserNotExistException {
        return userDao.getUser(id, type);
    }

    @Override
    public Map<String, Spot> getAllSpotIdMap() throws UserNotExistException {
        Map<String, Spot> result = new HashMap<>();
        for (User user : userDao.getAllUser(UserType.SPOT)) {
            result.put(user.getId(), (Spot) user);
        }
        return result;
    }

    @Override
    public Member memberConvertCoupon(Member member, Coupon coupon) throws MemberConvertCouponCreditNotEnoughException {
        // 检验会员剩余积分是否充足
        if (member.getCreditRemain() > coupon.getNeededCredit()) {
            List<Coupon> memberCoupons = member.getCoupons();
            memberCoupons.add(coupon);
            member.setCreditRemain(member.getCreditRemain() - coupon.getNeededCredit());

            boolean convertResult = userDao.updateUser(member, UserType.MEMBER);
            assert convertResult;
            return member;
        } else {
            throw new MemberConvertCouponCreditNotEnoughException();
        }
    }

    @Override
    public ContentMemberOfSpot getMemberOfSpot(String mid) throws UserNotExistException {
        Member result = (Member) userDao.getUser(mid, UserType.MEMBER);
        return new ContentMemberOfSpot(result);
    }
}
