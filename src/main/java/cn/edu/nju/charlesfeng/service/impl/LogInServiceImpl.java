package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserHasBeenSignUpException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class LogInServiceImpl implements LogInService {

    private final UserDao dao;

    @Autowired
    public LogInServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public Member registerMember(String id, String pwd, String email) throws UserNotExistException, UserHasBeenSignUpException {
        List<String> allMemberIds = getAllIds(dao.getAllUser(UserType.MEMBER));
        if (allMemberIds.indexOf(id) >= 0) throw new UserHasBeenSignUpException();
        else {
            Member newMember = new Member(id, pwd, email);
            dao.saveUser(newMember, UserType.MEMBER);
            return (Member) dao.getUser(id, UserType.MEMBER);
        }
    }

    // TODO 生成id
    @Override
    public Spot registerSpot(String pwd, String spotName, String site, List<SeatInfo> seatInfos, List<Seat> seats) throws UserNotExistException {
        // TODO seat_id生成策略（见 UserDaoImplTest:134~137）
        Spot newSpot = new Spot(pwd, spotName, false, site, seatInfos, JSON.toJSONString(seats));
        String curSpotId = dao.saveUser(newSpot, UserType.SPOT);
        return (Spot) dao.getUser(curSpotId, UserType.SPOT);
    }

    @Override
    public User logIn(String id, String pwd, UserType userType) throws UserNotExistException, WrongPwdException {
        User user = dao.getUser(id, userType);
        if (user.getPwd().equals(pwd)) return user;
        else throw new WrongPwdException();
    }

    /**
     * 获取用户的所有Id
     */
    private List<String> getAllIds(List<User> users) {
        List<String> ids = new LinkedList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }
}
