package cn.edu.nju.charlesfeng;

import cn.edu.nju.charlesfeng.controller.TestController;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Shenmiu
 * @date 2018/06/27
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
public class AddressTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void testExample() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/address")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
