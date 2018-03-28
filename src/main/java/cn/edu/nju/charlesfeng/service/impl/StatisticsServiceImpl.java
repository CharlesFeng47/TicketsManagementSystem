package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.ConsumptionDao;
import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Consumption;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.SingleOrderNumOfOneState;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.StatisticsService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final ConsumptionDao consumptionDao;

    private final OrderDao orderDao;

    @Autowired
    public StatisticsServiceImpl(ConsumptionDao consumptionDao, OrderDao orderDao) {
        this.consumptionDao = consumptionDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<Consumption> checkConsumption(User user) {
        List<Consumption> result = new LinkedList<>();

        if (user instanceof Member) {
            final String memberId = user.getId();
            for (Consumption cur : consumptionDao.getAllConsumption()) {
                if (cur.getMid().equals(memberId)) result.add(cur);
            }
        } else if ((user instanceof Spot)) {
            final String spotId = user.getId();
            for (Consumption cur : consumptionDao.getAllConsumption()) {
                if (cur.getSpot().getId().equals(spotId)) result.add(cur);
            }
        }
        return result;
    }

    @Override
    public List<SingleOrderNumOfOneState> checkOrders(User user) {
        // 初始化对应的 map
        Map<OrderState, Integer> stateNumMap = new HashMap<>();
        for (OrderState state : OrderState.values()) {
            stateNumMap.put(state, 0);
        }

        List<Order> allOrders = orderDao.getAllOrders();
        if (user instanceof Member) {
            final String memberId = user.getId();

            for (Order cur : allOrders) {
                if (cur.getMember().getId().equals(memberId)) {
                    OrderState curState = cur.getOrderState();
                    int num = stateNumMap.get(curState);
                    num++;
                    stateNumMap.put(curState, num);
                }
            }

        } else if ((user instanceof Spot)) {
            final String spotId = user.getId();
            for (Order cur : allOrders) {
                if (cur.getSchedule().getSpot().getId().equals(spotId)) {
                    OrderState curState = cur.getOrderState();
                    int num = stateNumMap.get(curState);
                    num++;
                    stateNumMap.put(curState, num);
                }
            }
        }

        List<SingleOrderNumOfOneState> result = new LinkedList<>();
        for (Map.Entry<OrderState, Integer> entry : stateNumMap.entrySet()) {
            result.add(new SingleOrderNumOfOneState(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
