package cn.edu.nju.charlesfeng.util.testUtil;

import cn.edu.nju.charlesfeng.dao.AlipayDao;
import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import org.springframework.context.ApplicationContext;

/**
 * 获取dao层对象，以便测试
 */
public class DaoTestHelper {

    /**
     * UserDao Bean
     */
    public static final UserDao userDao;

    /**
     * ScheduleDao Bean
     */
    public static final ScheduleDao scheduleDao;

    /**
     * OrderDao Bean
     */
    public static final OrderDao orderDao;

    /**
     * AlipayDao Bean
     */
    public static final AlipayDao alipayDao;

    static {
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
        userDao = applicationContext.getBean(UserDao.class);
        scheduleDao = applicationContext.getBean(ScheduleDao.class);
        orderDao = applicationContext.getBean(OrderDao.class);
        alipayDao = applicationContext.getBean(AlipayDao.class);
    }
}
