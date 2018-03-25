package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.service.MailService;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import cn.edu.nju.charlesfeng.util.testUtil.ServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * MailServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 25, 2018</pre>
 */
public class MailServiceImplTest {

    private MailService service;

    private UserDao userDao;

    @Before
    public void before() throws Exception {
        service = ServiceTestHelper.mailService;
        userDao = DaoTestHelper.userDao;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: sendMail(Member toSend)
     */
    @Test
    public void testSendMail() throws Exception {
        Member member = (Member) userDao.getUser("suzy", UserType.MEMBER);
        service.sendMail(member);
    }


} 
