package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.AlipayDao;
import cn.edu.nju.charlesfeng.entity.AlipayEntity;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * AlipayDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 21, 2018</pre>
 */
public class AlipayDaoImplTest {

    private AlipayDao dao;

    @Before
    public void before() throws Exception {
        dao = DaoTestHelper.alipayDao;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getAlipayEntity(String aliId)
     */
    @Test
    public void testGetAlipayEntity() throws Exception {
        AlipayEntity entity = dao.getAlipayEntity("suzy");
        Assert.assertEquals("qwertyuiop123456", entity.getPwd());
    }

    /**
     * Method: update(AlipayEntity alipayEntity)
     */
    @Test
    public void testUpdate() throws Exception {
        AlipayEntity entity = dao.getAlipayEntity("suzy");
        entity.setBalance(31111.5);
        dao.update(entity);
    }


} 
