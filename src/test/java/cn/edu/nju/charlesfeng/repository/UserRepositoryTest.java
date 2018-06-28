package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.task.MD5Task;
import cn.edu.nju.charlesfeng.util.helper.ImageHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testAdd() {
        User user = new User();
        user.setEmail("151250043@smail.nju.edu.cn");
        user.setPassword(MD5Task.encodeMD5("qwertyuiop"));
        user.setName("龚尘淼");
        user.setActivated(true);
        user.setPortrait(ImageHelper.getBaseImg(Objects.requireNonNull(this.getClass().getClassLoader().getResource("default.png")).getPath()));
        System.out.println("开始添加");
        userRepository.saveAndFlush(user);
        System.out.println("添加结束");
    }

    @Test
    public void testModify() {

    }

    @Test
    public void testGet() {
        try {
            User user = userRepository.getOne("1234567890@qq.com");
            Assert.assertEquals("byron", user.getName());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            System.out.println("未找到实体");
        }
        //createImg(user.getPortrait());
    }

    @Test
    public void testDelete() {
    }

    private void createImg(String img) {
        if (img == null) //图像数据为空
            return;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(img);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "F://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBaseImg() {
        File img = new File("F:\\img.jpg");
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(img);
            data = new byte[in.available()];
            int result = in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        assert data != null;
        return encoder.encode(data);
    }
}