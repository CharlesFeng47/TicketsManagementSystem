package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Consumption;
import cn.edu.nju.charlesfeng.model.ContentConsumption;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.StatisticsService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

/**
 * 对统计数据提供的控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("consumption")
    public RequestReturnObject getConsumption(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /statistics/consumption");

        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute(token);

        List<Consumption> result = statisticsService.checkConsumption(curUser);
        return new RequestReturnObject(RequestReturnObjectState.OK, convertToContentConsumption(result));
    }

    private List<ContentConsumption> convertToContentConsumption(List<Consumption> consumptions) {
        List<ContentConsumption> result = new LinkedList<>();
        for (Consumption cur : consumptions) {
            result.add(new ContentConsumption(cur));
        }
        return result;
    }
}
