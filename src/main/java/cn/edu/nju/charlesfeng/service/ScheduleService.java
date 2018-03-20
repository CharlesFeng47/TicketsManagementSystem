package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;

import java.time.LocalDateTime;
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
     * @param name               欲发布的活动日程名称
     * @param spot               欲发布的活动日程基于的场馆
     * @param startDateTime      欲发布的活动日程开始时间
     * @param scheduleItemType   欲发布的活动日程类型
     * @param seatInfoPricesJson 欲发布的活动日程座位类型价格映射
     * @param description        欲发布的活动日程描述
     * @return 是否成功发布，成功则为该实体对象
     */
    Schedule publishSchedule(String name, Spot spot, LocalDateTime startDateTime, ScheduleItemType scheduleItemType,
                             String seatInfoPricesJson, String description);

    /**
     * @param scheduleId         欲修改的活动日程ID
     * @param name               欲修改的活动日程名称
     * @param spot               欲修改的活动日程基于的场馆
     * @param startDateTime      欲修改的活动日程开始时间
     * @param scheduleItemType   欲修改的活动日程类型
     * @param seatInfoPricesJson 欲修改的活动日程座位类型价格映射
     * @param description        欲修改的活动日程描述
     * @return 是否成功修改，成功则true
     */
    boolean modifySchedule(String scheduleId, String name, Spot spot, LocalDateTime startDateTime, ScheduleItemType scheduleItemType,
                           String seatInfoPricesJson, String description);

    /**
     * 对一次日程活动的检票
     * TODO 需求不明确
     */
    boolean checkTickets();
}
