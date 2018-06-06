package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.AlipayAccount;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlipayRepositoryTest {

    @Autowired
    private AlipayRepository alipayRepository;

    @Test
    public void testAdd(){
        AlipayAccount account = new AlipayAccount();
        account.setId("1234567890@qq.com");
        account.setPwd("qwertyuiop");
        account.setBalance(10000);
        alipayRepository.save(account);
    }

    @Test
    public void testModify(){
        AlipayAccount account = new AlipayAccount();
        account.setId("1234567890@qq.com");
        account.setPwd("qwertyuiop");
        account.setBalance(50000);
        alipayRepository.save(account);
    }

    @Test
    public void testGet(){
        AlipayAccount alipayAccount = alipayRepository.getOne("1234567890@qq.com");
        Assert.assertEquals(50000.0,alipayAccount.getBalance(), 0);
        Assert.assertEquals("qwertyuiop", alipayAccount.getPwd());
    }

    @Test
    public void testDelete(){
        alipayRepository.deleteById("1234567890@qq.com");
    }

}