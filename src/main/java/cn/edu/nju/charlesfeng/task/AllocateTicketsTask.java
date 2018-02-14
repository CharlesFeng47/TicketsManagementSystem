package cn.edu.nju.charlesfeng.task;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 演出前2周开票/配票
 */
@Component
public class AllocateTicketsTask {

    private Logger logger = Logger.getLogger(AllocateTicketsTask.class);

    // 0点自动更新配票信息
    @Scheduled(cron = "0 0 0 * * ?")
    public void allocate() {
        logger.info("配票任务已执行");
    }
}
