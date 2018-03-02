package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleDao scheduleDao;

    @Autowired
    public ScheduleServiceImpl(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleDao.getAllSchedules();
    }

    @Override
    public List<Schedule> getSchedulesOfOneSpot(String spotId) {
        List<Schedule> result = new LinkedList<>();
        List<Schedule> allSchedule = getAllSchedules();
        for (Schedule schedule : allSchedule) {
            if (schedule.getSpotId().equals(spotId)) {
                result.add(schedule);
            }
        }
        return result;
    }

    @Override
    public Schedule getOneSchedule(int sid) {
        return scheduleDao.getSchedule(sid);
    }

    @Override
    public Schedule publishSchedule(Schedule schedule) {
        int scheduleId = scheduleDao.saveSchedule(schedule);
        schedule.setId(scheduleId);
        return schedule;
    }

    @Override
    public boolean checkTickets() {
        return false;
    }
}
