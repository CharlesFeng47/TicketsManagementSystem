package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Consumption;
import cn.edu.nju.charlesfeng.model.User;

import java.util.List;

/**
 * 为系统提供统计服务
 */
public interface StatisticsService {

    /**
     * @param user 要查看的会员／场馆
     * @return 会员本人的消费记录
     */
    List<Consumption> checkConsumption(User user);
}
