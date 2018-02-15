package cn.edu.nju.charlesfeng.util.testUtil;

import cn.edu.nju.charlesfeng.dao.UserDao;
import org.springframework.context.ApplicationContext;

/**
 * 获取dao层对象，以便测试
 */
public class DaoTestHelper {

    public static final UserDao userDao;

    static {
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
        userDao = applicationContext.getBean(UserDao.class);
    }
}
