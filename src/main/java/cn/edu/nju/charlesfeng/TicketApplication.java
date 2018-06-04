package cn.edu.nju.charlesfeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Shenmiu
 * @date 2018/06/04
 * @SpringBootApplication: 自动扫描配置文件和Component
 * @EnableScheduling: 创建task executor，执行@Schedule任务
 */
@SpringBootApplication
@EnableScheduling
public class TicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }

}
