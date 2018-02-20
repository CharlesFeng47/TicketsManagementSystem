package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public ScheduleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Schedule getSchedule(int scheduleId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Schedule result = session.find(Schedule.class, scheduleId);
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public int saveSchedule(Schedule schedule) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        int scheduleId = (int) session.save(schedule);
        tx.commit();
        session.close();
        return scheduleId;
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(schedule);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean deleteSchedule(Schedule schedule) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(schedule);
        tx.commit();
        session.close();
        return true;
    }
}
