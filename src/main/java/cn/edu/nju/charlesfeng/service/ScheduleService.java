package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.ScheduleItem;

/**
 * 系统中活动日程的服务
 */
public interface ScheduleService {

    /**
     * @param scheduleItem 欲发布的活动日程
     * @return 是否成功发布，成功则true
     */
    boolean publishScheduleItem(ScheduleItem scheduleItem);

    /**
     * 对一次日程活动的检票
     * TODO 需求不明确
     */
    boolean checkTickets();
}
