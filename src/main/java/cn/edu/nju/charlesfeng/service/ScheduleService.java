package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Schedule;

import java.util.List;

/**
 * 系统中活动日程的服务
 */
public interface ScheduleService {

    /**
     * @return 所有日程
     */
    List<Schedule> getAllSchedules();

    /**
     * @param spotId 欲获取的相关场馆的场馆Id
     * @return 相关场馆的所有日程
     */
    List<Schedule> getSchedulesOfOneSpot(String spotId);

    /**
     * @param sid 欲获取的日程Id
     * @return 日程详情实体
     */
    Schedule getOneSchedule(String sid);

    /**
     * @param sid 欲删除的日程Id
     * @return 删除结果，若成功删除则true
     */
    boolean deleteOneSchedule(String sid);

    /**
     * @param schedule 欲发布的活动日程
     * @return 是否成功发布，成功则为该实体对象
     */
    Schedule publishSchedule(Schedule schedule);

    /**
     * @param schedule 欲修改的活动日程
     * @return 是否成功修改，成功则true
     */
    boolean modifySchedule(Schedule schedule);

    /**
     * 对一次日程活动的检票
     * TODO 需求不明确
     */
    boolean checkTickets();
}
