//package cn.edu.nju.charlesfeng.dao.impl;
//
//import cn.edu.nju.charlesfeng.dao.BaseDao;
//import cn.edu.nju.charlesfeng.dao.ScheduleDao;
//import cn.edu.nju.charlesfeng.model.Schedule;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class ScheduleDaoImpl implements ScheduleDao {
//
//    private final BaseDao baseDao;
//
//    @Autowired
//    public ScheduleDaoImpl(BaseDao baseDao) {
//        this.baseDao = baseDao;
//    }
//
//    @Override
//    public Schedule getSchedule(String scheduleId) {
//        return (Schedule) baseDao.get(Schedule.class, scheduleId);
//    }
//
//    @Override
//    public List<Schedule> getAllSchedules() {
//        return baseDao.getAllList(Schedule.class);
//    }
//
//    @Override
//    public String saveSchedule(Schedule schedule) {
//        return (String) baseDao.save(schedule);
//    }
//
//    @Override
//    public boolean updateSchedule(Schedule schedule) {
//        return baseDao.update(schedule);
//    }
//
//    @Override
//    public boolean deleteSchedule(String scheduleId) {
//        return baseDao.delete(Schedule.class, scheduleId);
//    }
//}
