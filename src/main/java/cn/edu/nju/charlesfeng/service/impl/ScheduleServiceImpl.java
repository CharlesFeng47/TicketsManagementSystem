package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Schedule getOneSchedule(String sid) {
        return scheduleDao.getSchedule(sid);
    }

    @Override
    public boolean deleteOneSchedule(String sid) {
        return scheduleDao.deleteSchedule(sid);
    }

    @Override
    public Schedule publishSchedule(Schedule schedule) {
        // TODO seat_id生成策略（见 UserDaoImplTest:134~137）
        schedule.setId(generateId());
        scheduleDao.saveSchedule(schedule);
        return schedule;
    }

    @Override
    public boolean checkTickets() {
        return false;
    }

    /**
     * 通过当前时间产生日程ID
     */
    private String generateId() {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(now.getYear()).append(now.getMonthValue()).append(now.getDayOfMonth());
        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
        return sb.toString();
    }
}
