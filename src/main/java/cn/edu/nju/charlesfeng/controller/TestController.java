package cn.edu.nju.charlesfeng.controller;

import org.springframework.web.bind.annotation.*;

/**
 * 为前端系统提供联通检测
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static int counter = 0;

    @GetMapping
    public String testGet() {
        counter++;
        System.out.println("被第" + counter + "次访问！");
        return "test success get";
    }

    @PostMapping
    public String testPost() {
        counter++;
        System.out.println("被第" + counter + "次访问！");
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
