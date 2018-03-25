package cn.edu.nju.charlesfeng.task;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.util.enums.ScheduleState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 对计划的操作
 */
@Component
public class ScheduleTask {

    private final static Logger logger = Logger.getLogger(ScheduleTask.class);

    private final ScheduleDao scheduleDao;

    @Autowired
    public ScheduleTask(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    /**
     * 到达开始时间的计划自动置为已完成
     */
    @Scheduled(cron = "0 25 17 * * ?")
    public void ScheduleAutoComplete() {
        logger.info("ScheduleAutoComplete Task 开始工作");


        final LocalDateTime curTime = LocalDateTime.now();
        for (Schedule curSchedule : scheduleDao.getAllSchedules()) {
            if (curTime.isAfter(curSchedule.getStartDateTime())) {
                curSchedule.setState(ScheduleState.COMPLETED);
                scheduleDao.updateSchedule(curSchedule);
            }
        }
    }
}
