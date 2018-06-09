//package cn.edu.nju.charlesfeng.service.impl;
//
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
