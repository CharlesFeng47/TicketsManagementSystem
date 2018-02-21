package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    private final BaseDao baseDao;

    @Autowired
    public ScheduleDaoImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Schedule getSchedule(int scheduleId) {
        return (Schedule) baseDao.get(Schedule.class, scheduleId);
    }

    @Override
    public int saveSchedule(Schedule schedule) {
        return (int) baseDao.save(schedule);
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        return baseDao.update(schedule);
    }

    @Override
    public boolean deleteSchedule(int scheduleId) {
        return baseDao.delete(Schedule.class, scheduleId);
    }
}
