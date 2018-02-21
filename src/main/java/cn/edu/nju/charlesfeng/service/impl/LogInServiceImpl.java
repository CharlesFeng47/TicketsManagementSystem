package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LogInServiceImpl implements LogInService {

    private final UserDao dao;

    @Autowired
    public LogInServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public Member registerMember(String id, String pwd) throws UserNotExistException {
        Member newMember = new Member(id, pwd);
        dao.saveUser(newMember, UserType.MEMBER);
        return (Member) dao.getUser(id, UserType.MEMBER);
    }

    @Override
    public Spot registerSpot(String id, String pwd, String site, Set<SeatInfo> seatInfos, Set<Seat> seats) throws UserNotExistException {
        Spot newSpot = new Spot(id, pwd, false, site, seatInfos, JSON.toJSONString(seats));
        dao.saveUser(newSpot, UserType.SPOT);
        return (Spot) dao.getUser(id, UserType.SPOT);
    }

    @Override
    public User logIn(String id, String pwd, UserType userType) throws UserNotExistException, WrongPwdException {
        User user = dao.getUser(id, userType);
        if (user.getPwd().equals(pwd)) return user;
        else throw new WrongPwdException();
    }
}
