package cn.edu.nju.charlesfeng.dao;

import cn.edu.nju.charlesfeng.entity.Schedule;

/**
 * 数据层对日程计划的服务
 */
public interface ScheduleDao {

    /**
     * @param scheduleId 要查看的日程ID
     * @return 该日程的实体对象
     */
    Schedule getSchedule(int scheduleId);

    /**
     * @param schedule 欲保存的日程计划实体
     * @return 成功保存后的此实体对象主键
     */
    int saveSchedule(Schedule schedule);

    /**
     * @param schedule 欲更新的日程计划实体
     * @return 更新结果，成功则true
     */
    boolean updateSchedule(Schedule schedule);

    /**
     * @param schedule 欲删除的日程计划实体
     * @return 删除结果，成功则true
     */
    boolean deleteSchedule(Schedule schedule);
}
