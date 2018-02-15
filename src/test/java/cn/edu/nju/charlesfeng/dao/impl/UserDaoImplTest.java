package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Manager;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * UserDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 15, 2018</pre>
 */
public class UserDaoImplTest {

    private Logger logger = Logger.getLogger(UserDaoImplTest.class);

    private UserDao dao;

    @Before
    public void before() throws Exception {
        dao = DaoTestHelper.userDao;
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetUser() throws Exception {
        Manager adminManager = (Manager) dao.getUser("admin", UserType.MANAGER);
        logger.debug(adminManager);
    }

    @Test
    public void testManager() throws Exception {
        Manager manager1 = new Manager("cuihua", "qwertyuiop123456");
        boolean saveResult = dao.saveUser(manager1, UserType.MANAGER);
        logger.debug(saveResult);

        Manager manager2 = new Manager("cuihua", "qwertyuiop");
        boolean updateResult = dao.updateUser(manager2, UserType.MANAGER);
        logger.debug(updateResult);

        Manager resultManager = (Manager) dao.getUser("cuihua", UserType.MANAGER);
        logger.debug(resultManager);
    }

    @Test
    public void testMember() throws Exception {
        Member member1 = new Member("suzy", "qwertyuiop123456");
        boolean saveResult = dao.saveUser(member1, UserType.MEMBER);
        logger.debug(saveResult);

        Member member2 = new Member("suzy", "qwertyuiop");
        boolean updateResult = dao.updateUser(member2, UserType.MEMBER);
        logger.debug(updateResult);

        Member resultMember = (Member) dao.getUser("suzy", UserType.MEMBER);
        logger.debug(resultMember);
    }

}
