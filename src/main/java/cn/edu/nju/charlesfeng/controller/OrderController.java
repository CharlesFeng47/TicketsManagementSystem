package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

/**
 * 对订单信息的控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    /**
     * 订购
     */
    @PostMapping
    public RequestReturnObject order() {
        logger.debug("INTO /order");
        return null;
    }

    /**
     * 获取单条订单
     */
    @GetMapping("/{oid}")
    public RequestReturnObject getOneOrder(@PathVariable("oid") String oidRepre) {
        logger.debug("INTO /order/" + oidRepre);
        return null;
    }
}
