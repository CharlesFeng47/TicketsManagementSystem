package cn.edu.nju.charlesfeng;

import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.alibaba.fastjson.support.spring.FastJsonpResponseBodyAdvice;
import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Shenmiu
 * @date 2018/06/04
 * @SpringBootApplication: 自动扫描配置文件和Component
 * @EnableScheduling: 创建task executor，执行@Schedule任务
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class TicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
}
