package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
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
            if (schedule.getSpot().getId().equals(spotId)) {
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
    public Schedule publishSchedule(String name, Spot curSpot, LocalDateTime startDateTime, ScheduleItemType scheduleItemType,
                                    String seatInfoPricesJson, String description) {
        Schedule toSave = new Schedule();
        toSave.setId(generateId());
        toSave.setName(name);
        toSave.setSpot(curSpot);
        toSave.setStartDateTime(startDateTime);
        toSave.setType(scheduleItemType);
        toSave.setSeatInfoPricesJson(seatInfoPricesJson);
        toSave.setDescription(description);

        // 默认的剩余座位就是场馆的座位图
        toSave.setRemainSeatsJson(curSpot.getAllSeatsJson());
        // 默认无已预订座位，初始化为空值
        toSave.setBookedSeatsIdJson("[]");

        scheduleDao.saveSchedule(toSave);
        return toSave;
    }

    @Override
    public boolean modifySchedule(String scheduleId, String name, Spot curSpot, LocalDateTime startDateTime,
                                  ScheduleItemType scheduleItemType, String seatInfoPricesJson, String description) {
        Schedule toModify = scheduleDao.getSchedule(scheduleId);
        toModify.setName(name);
        toModify.setSpot(curSpot);
        toModify.setStartDateTime(startDateTime);
        toModify.setType(scheduleItemType);
        toModify.setSeatInfoPricesJson(seatInfoPricesJson);
        toModify.setDescription(description);
        return scheduleDao.updateSchedule(toModify);
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
