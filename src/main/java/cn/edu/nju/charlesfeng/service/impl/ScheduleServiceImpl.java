//package cn.edu.nju.charlesfeng.service.impl;
//
//import cn.edu.nju.charlesfeng.dao.AlipayRepository;
//import cn.edu.nju.charlesfeng.dao.ScheduleDao;
//import cn.edu.nju.charlesfeng.model.AlipayAccount;
//import cn.edu.nju.charlesfeng.model.Schedule;
//import cn.edu.nju.charlesfeng.model.Spot;
//import cn.edu.nju.charlesfeng.service.ScheduleService;
//import cn.edu.nju.charlesfeng.util.enums.ProgramType;
//import cn.edu.nju.charlesfeng.util.enums.ScheduleState;
//import cn.edu.nju.charlesfeng.util.exceptions.ScheduleNotSettlableException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.LinkedList;
//import java.util.List;
//
//@Service
//public class ScheduleServiceImpl implements ScheduleService {
//
//    private final ScheduleDao scheduleDao;
//
//    private final AlipayRepository alipayDao;
//
//    @Autowired
//    public ScheduleServiceImpl(ScheduleDao scheduleDao, AlipayRepository alipayDao) {
//        this.scheduleDao = scheduleDao;
//        this.alipayDao = alipayDao;
//    }
//
//    @Override
//    public List<Schedule> getAllSchedules() {
//        return scheduleDao.getAllSchedules();
//    }
//
//    @Override
//    public List<Schedule> getAllAvailableSchedules() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Schedule> result = new LinkedList<>();
//        List<Schedule> allSchedule = scheduleDao.getAllSchedules();
//        for (Schedule schedule : allSchedule) {
//            if (schedule.getStartDateTime().isAfter(now)) {
//                result.add(schedule);
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public List<Schedule> getSchedulesOfOneSpot(String spotId) {
//        List<Schedule> result = new LinkedList<>();
//        List<Schedule> allSchedule = scheduleDao.getAllSchedules();
//        for (Schedule schedule : allSchedule) {
//            if (schedule.getSpot().getId().equals(spotId)) {
//                result.add(schedule);
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public Schedule getOneSchedule(String sid) {
//        return scheduleDao.getSchedule(sid);
//    }
//
//    @Override
//    public boolean deleteOneSchedule(String sid) {
//        return scheduleDao.deleteSchedule(sid);
//    }
//
//    @Override
//    public Schedule publishSchedule(String name, Spot curSpot, LocalDateTime startDateTime, ProgramType scheduleItemType,
//                                    String seatInfoPricesJson, String description) {
//        Schedule toSave = new Schedule();
//        toSave.setId(generateId());
//        toSave.setName(name);
//        toSave.setSpot(curSpot);
//        toSave.setStartDateTime(startDateTime);
//        toSave.setType(scheduleItemType);
//        toSave.setSeatInfoPricesJson(seatInfoPricesJson);
//        toSave.setDescription(description);
//
//        // 默认的剩余座位就是场馆的座位图
//        toSave.setRemainSeatsJson(curSpot.getAllSeatsJson());
//        // 默认无已预订座位，初始化为空值
//        toSave.setBookedSeatsIdJson("[]");
//        // 默认为已发布
//        toSave.setState(ScheduleState.RELEASED);
//        // 默认已经购买的金额／未结算金额为0
//        toSave.setBalance(0);
//
//        scheduleDao.saveSchedule(toSave);
//        return toSave;
//    }
//
//    @Override
//    public boolean modifySchedule(String scheduleId, String name, Spot curSpot, LocalDateTime startDateTime,
//                                  ProgramType scheduleItemType, String seatInfoPricesJson, String description) {
//        Schedule toModify = scheduleDao.getSchedule(scheduleId);
//        toModify.setName(name);
//        toModify.setSpot(curSpot);
//        toModify.setStartDateTime(startDateTime);
//        toModify.setType(scheduleItemType);
//        toModify.setSeatInfoPricesJson(seatInfoPricesJson);
//        toModify.setDescription(description);
//        return scheduleDao.updateSchedule(toModify);
//    }
//
//    @Override
//    public boolean settleOneSchedule(String scheduleId) throws ScheduleNotSettlableException {
//        // 结算的比例
//        final double settlePercent = 0.8;
//
//        Schedule toSettle = scheduleDao.getSchedule(scheduleId);
//
//        if (toSettle.getState() == ScheduleState.COMPLETED) {
//            // 可以被结算
//            // 计划状态改变
//            toSettle.setState(ScheduleState.SETTLED);
//
//            // 计划中会员支付的金额被结算
//            Spot settledSpot = toSettle.getSpot();
//            AlipayAccount alipayAccount = alipayDao.getAlipayEntity(settledSpot.getAlipayId());
//            alipayAccount.setBalance(alipayAccount.getBalance() + toSettle.getBalance() * settlePercent);
//            alipayDao.update(alipayAccount);
//        } else throw new ScheduleNotSettlableException();
//        return scheduleDao.updateSchedule(toSettle);
//    }
//
//    /**
//     * 通过当前时间产生日程ID
//     */
//    private String generateId() {
//        LocalDateTime now = LocalDateTime.now();
//        StringBuilder sb = new StringBuilder();
//        sb.append(now.getYear()).append(now.getMonthValue()).append(now.getDayOfMonth());
//        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
//        return sb.toString();
//    }
//}
