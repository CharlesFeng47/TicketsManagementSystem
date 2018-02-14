package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean activateByMail() {
        return false;
    }

    @Override
    public boolean invalidate(String mid) {
        return false;
    }

    @Override
    public boolean modifyUser(User user) {
        return false;
    }

    @Override
    public User getUser(String id, UserType type) throws UserNotExistException {
        return userDao.getUser(id, type);
    }
}
