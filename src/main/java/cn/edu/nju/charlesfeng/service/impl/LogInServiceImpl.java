//package cn.edu.nju.charlesfeng.service.impl;
//
//import cn.edu.nju.charlesfeng.dao.AlipayRepository;
//import cn.edu.nju.charlesfeng.dao.UserRepository;
//import cn.edu.nju.charlesfeng.model.AlipayAccount;
//import cn.edu.nju.charlesfeng.model.Member;
//import cn.edu.nju.charlesfeng.model.SeatInfo;
//import cn.edu.nju.charlesfeng.model.Spot;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.service.LogInService;
//import cn.edu.nju.charlesfeng.service.MailService;
//import cn.edu.nju.charlesfeng.util.IdGenerator;
//import cn.edu.nju.charlesfeng.util.enums.UserType;
//import cn.edu.nju.charlesfeng.util.exceptions.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//
//@Service
//public class LogInServiceImpl implements LogInService {
//
//    private final MailService mailService;
//
//    private final UserRepository userDao;
//
//    private final AlipayRepository alipayDao;
//
//    @Autowired
//    public LogInServiceImpl(MailService mailService, UserRepository dao, AlipayRepository alipayDao) {
//        this.mailService = mailService;
//        this.userDao = dao;
//        this.alipayDao = alipayDao;
//    }
//
//    @Override
//    public Member registerMember(String id, String pwd, String email) throws UserNotExistException, UserHasBeenSignUpException, InteriorWrongException {
//        List<String> allMemberIds = getAllIds(userDao.getAllUser(UserType.MEMBER));
//        if (allMemberIds.indexOf(id) >= 0) throw new UserHasBeenSignUpException();
//        else {
//            Member newMember = new Member(id, pwd, email);
//            userDao.saveUser(newMember, UserType.MEMBER);
//
//            // 注册成功后发送邮箱链接
//            Member curMember = (Member) userDao.getUser(id, UserType.MEMBER);
//            try {
//                mailService.sendMail(curMember);
//            } catch (IOException | MessagingException e) {
//                throw new InteriorWrongException();
//            }
//
//            return curMember;
//        }
//    }
//
//    @Override
//    public Spot registerSpot(String pwd, String spotName, String site, String alipayId,
//                             List<SeatInfo> seatInfos, String seatsMapJson, int curSeatTypeCount) throws UserNotExistException, AlipayEntityNotExistException {
//        AlipayAccount alipayAccount = alipayDao.getAlipayEntity(alipayId);
//        if (alipayAccount == null) throw new AlipayEntityNotExistException();
//
//        String sidBase = IdGenerator.generateSeatId();
//        for (int i = 0; i < seatInfos.size(); i++) {
//            SeatInfo curSeatInfo = seatInfos.get(i);
//            curSeatInfo.setId(sidBase + "/" + i);
//        }
//
//        String spotId;
//        List<String> allSpotIds = getAllIds(userDao.getAllUser(UserType.SPOT));
//        while (true) {
//            int curRandom = (int) (Math.random() * 10000000);
//            spotId = convertToSevenId(curRandom);
//            if (allSpotIds.indexOf(spotId) < 0) break;
//        }
//
//        Spot newSpot = new Spot();
//        newSpot.setId(spotId);
//        newSpot.setPwd(pwd);
//        newSpot.setSpotName(spotName);
//        newSpot.setExamined(false);
//        newSpot.setSite(site);
//        newSpot.setAlipayId(alipayId);
//        newSpot.setSeatInfos(seatInfos);
//        newSpot.setAllSeatsJson(seatsMapJson);
//        newSpot.setCurSeatTypeCount(curSeatTypeCount);
//
//        String curSpotId = userDao.saveUser(newSpot, UserType.SPOT);
//        return (Spot) userDao.getUser(curSpotId, UserType.SPOT);
//    }
//
//    @Override
//    public User logIn(String id, String pwd, UserType userType) throws UserNotExistException, WrongPwdException {
//        User user = userDao.getUser(id, userType);
//        if (user.getPwd().equals(pwd)) return user;
//        else throw new WrongPwdException();
//    }
//
//    /**
//     * 获取用户的所有Id
//     */
//    private List<String> getAllIds(List<User> users) {
//        List<String> ids = new LinkedList<>();
//        for (User user : users) {
//            ids.add(user.getId());
//        }
//        return ids;
//    }
//
//    private String convertToSevenId(int id) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(id);
//        while (sb.length() < 7) {
//            sb.insert(0, '0');
//        }
//        return sb.toString();
//    }
//}
