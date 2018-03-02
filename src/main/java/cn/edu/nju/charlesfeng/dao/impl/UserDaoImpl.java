package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Manager;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final BaseDao baseDao;

    @Autowired
    public UserDaoImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public User getUser(String id, UserType type) throws UserNotExistException {
        User result;

        if (type == UserType.MEMBER) result = (User) baseDao.get(Member.class, id);
        else if (type == UserType.SPOT) result = (User) baseDao.get(Spot.class, id);
        else result = (User) baseDao.get(Manager.class, id);

        if (result == null) throw new UserNotExistException();
        return result;
    }

    @Override
    public List<User> getAllUser(UserType type) throws UserNotExistException {
        List<User> result;

        if (type == UserType.MEMBER) result = baseDao.getAllList(Member.class);
        else if (type == UserType.SPOT) result = baseDao.getAllList(Spot.class);
        else result = baseDao.getAllList(Manager.class);

        if (result == null) throw new UserNotExistException();
        return result;
    }

    @Override
    public String saveUser(User user, UserType type) {
        return (String) baseDao.save(user);
    }

    @Override
    public boolean updateUser(User user, UserType type) {
        return baseDao.update(user);
    }
}
