package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Schedule;

/**
 * 系统中活动日程的服务
 */
public interface ScheduleService {

    /**
     * @param sid 欲获取的日程Id
     * @return 日程详情实体
     */
    Schedule getOneSchedule(int sid);

    /**
     * @param scheduleItem 欲发布的活动日程
     * @return 是否成功发布，成功则为该实体对象
     */
    Schedule publishSchedule(Schedule scheduleItem);

    /**
     * 对一次日程活动的检票
     * TODO 需求不明确
     */
    boolean checkTickets();
}
