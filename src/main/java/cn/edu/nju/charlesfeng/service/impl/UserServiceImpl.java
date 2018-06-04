package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.AlipayDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.model.*;
import cn.edu.nju.charlesfeng.filter.ContentMemberOfSpot;
import cn.edu.nju.charlesfeng.filter.UnexaminedSpot;
import cn.edu.nju.charlesfeng.filter.User;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.AlipayEntityNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.MemberActiveUrlExpiredException;
import cn.edu.nju.charlesfeng.util.exceptions.MemberConvertCouponCreditNotEnoughException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final AlipayDao alipayDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, AlipayDao alipayDao) {
        this.userDao = userDao;
        this.alipayDao = alipayDao;
    }

    @Override
    public boolean activateByMail(String activeUrl) throws UnsupportedEncodingException, UserNotExistException, MemberActiveUrlExpiredException {
        byte[] base64decodedBytes = Base64.getUrlDecoder().decode(activeUrl);
        String toActivateMemberId = new String(base64decodedBytes, "utf-8");

        Member toActivate = (Member) userDao.getUser(toActivateMemberId, UserType.MEMBER);
        if (toActivate.isActivated()) throw new MemberActiveUrlExpiredException();
        toActivate.setActivated(true);
        return userDao.updateUser(toActivate, UserType.MEMBER);
    }

    @Override
    public boolean invalidate(Member member) {
        member.setInvalidated(true);
        return userDao.updateUser(member, UserType.MEMBER);
    }

    @Override
    public boolean modifyMember(Member curMember, String pwd) {
        curMember.setPwd(pwd);
        return userDao.updateUser(curMember, UserType.MEMBER);
    }

    @Override
    public boolean modifySpot(Spot curSpot, String pwd, String spotName, String site, String alipayId,
                              List<SeatInfo> seatInfos, String seatsMapJson, int curSeatTypeCount) throws AlipayEntityNotExistException {
        AlipayAccount alipayAccount = alipayDao.getAlipayEntity(alipayId);
        if (alipayAccount == null) throw new AlipayEntityNotExistException();

        // 修改场馆信息后之后需要经理审批
        curSpot.setExamined(false);

        curSpot.setPwd(pwd);
        curSpot.setSpotName(spotName);
        curSpot.setSite(site);
        curSpot.setAlipayId(alipayId);
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

    @Override
    public List<UnexaminedSpot> getAllUnexaminedSpots() throws UserNotExistException {
        List<UnexaminedSpot> result = new LinkedList<>();
        for (User curUser : userDao.getAllUser(UserType.SPOT)) {
            final Spot curSpot = (Spot) curUser;
            if (!curSpot.isExamined()) {
                result.add(new UnexaminedSpot(curSpot));
            }
        }
        return result;
    }

    @Override
    public boolean examineSpot(String sid) throws UserNotExistException {
        Spot toExamine = (Spot) userDao.getUser(sid, UserType.SPOT);
        toExamine.setExamined(true);
        return userDao.updateUser(toExamine, UserType.SPOT);
    }
}
