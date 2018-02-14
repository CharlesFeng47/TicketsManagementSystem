package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Manager;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private Logger logger = Logger.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUser(String id, UserType type) throws UserNotExistException {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        User result;

        if (type == UserType.MEMBER) result = session.find(Member.class, id);
        else if (type == UserType.SPOT) result = session.find(Spot.class, id);
        else result = session.find(Manager.class, id);

        if (result == null) throw new UserNotExistException();
        tx.commit();
        session.close();
        return result;
    }
}
