package cn.edu.nju.charlesfeng.util.testUtil;

import cn.edu.nju.charlesfeng.service.MailService;
import org.springframework.context.ApplicationContext;

public class ServiceTestHelper {

    /**
     * AlipayDao Bean
     */
    public static final MailService mailService;

    static {
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
        mailService = applicationContext.getBean(MailService.class);

    }
}
