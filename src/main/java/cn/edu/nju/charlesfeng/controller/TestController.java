package cn.edu.nju.charlesfeng.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 为前端系统提供联通检测
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = Logger.getLogger(TestController.class);

    private static int counter = 0;

    @GetMapping
    public String testGet() {
        counter++;
        System.out.println("被第" + counter + "次访问！");
        return "test success get";
    }

    @PostMapping
    public String testPost(@RequestParam("paramName") String testTransfer, HttpServletRequest request) {
        counter++;
        System.out.println("被第" + counter + "次访问！");
        logger.info(request.getParameterMap());
        logger.info(testTransfer);
        return "test success post";
    }

    @PutMapping
    public String testPut() {
        counter++;
        System.out.println("被第" + counter + "次访问！");
        return "test success put";
    }

    @DeleteMapping
    public String testDelete() {
        counter++;
        System.out.println("被第" + counter + "次访问！");
        return "test success delete";
    }
}
