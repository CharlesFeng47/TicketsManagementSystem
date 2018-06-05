//package cn.edu.nju.charlesfeng.service.impl;
//
//import cn.edu.nju.charlesfeng.dao.ConsumptionDao;
//import cn.edu.nju.charlesfeng.dao.OrderDao;
//import cn.edu.nju.charlesfeng.dao.ScheduleDao;
//import cn.edu.nju.charlesfeng.dao.UserDao;
//import cn.edu.nju.charlesfeng.model.*;
//import cn.edu.nju.charlesfeng.filter.IncomeOfOneSpot;
//import cn.edu.nju.charlesfeng.filter.NumOfOneMemberLevel;
//import cn.edu.nju.charlesfeng.filter.OrderNumOfOneState;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.service.StatisticsService;
//import cn.edu.nju.charlesfeng.util.enums.OrderState;
//import cn.edu.nju.charlesfeng.util.enums.UserType;
//import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class StatisticsServiceImpl implements StatisticsService {
//
//    private final ConsumptionDao consumptionDao;
//
//    private final OrderDao orderDao;
//
//    private final UserDao userDao;
//
//    private final ScheduleDao scheduleDao;
//
//    @Autowired
//    public StatisticsServiceImpl(ConsumptionDao consumptionDao, OrderDao orderDao, UserDao userDao, ScheduleDao scheduleDao) {
//        this.consumptionDao = consumptionDao;
//        this.orderDao = orderDao;
//        this.userDao = userDao;
//        this.scheduleDao = scheduleDao;
//    }
//
//    @Override
//    public List<Consumption> checkConsumption(User user) {
//        List<Consumption> result = new LinkedList<>();
//
//        if (user instanceof Member) {
//            final String memberId = user.getId();
//            for (Consumption cur : consumptionDao.getAllConsumption()) {
//                if (cur.getMid().equals(memberId)) result.add(cur);
//            }
//        } else if (user instanceof Spot) {
//            final String spotId = user.getId();
//            for (Consumption cur : consumptionDao.getAllConsumption()) {
//                if (cur.getSpot().getId().equals(spotId)) result.add(cur);
//            }
//        } else if (user instanceof Manager) {
//            result = consumptionDao.getAllConsumption();
//        }
//        return result;
//    }
//
//    @Override
//    public List<OrderNumOfOneState> checkOrders(User user) {
//        // 初始化对应的 map
//        Map<OrderState, Integer> stateNumMap = new HashMap<>();
//        for (OrderState state : OrderState.values()) {
//            stateNumMap.put(state, 0);
//        }
//
//        List<Order> allOrders = orderDao.getAllOrders();
//        if (user instanceof Member) {
//            final String memberId = user.getId();
//
//            for (Order cur : allOrders) {
//                if (cur.getMember().getId().equals(memberId)) {
//                    OrderState curState = cur.getOrderState();
//                    int num = stateNumMap.get(curState);
//                    num++;
//                    stateNumMap.put(curState, num);
//                }
//            }
//
//        } else if ((user instanceof Spot)) {
//            final String spotId = user.getId();
//            for (Order cur : allOrders) {
//                if (cur.getSchedule().getSpot().getId().equals(spotId)) {
//                    OrderState curState = cur.getOrderState();
//                    int num = stateNumMap.get(curState);
//                    num++;
//                    stateNumMap.put(curState, num);
//                }
//            }
//        }
//
//        List<OrderNumOfOneState> result = new LinkedList<>();
//        for (Map.Entry<OrderState, Integer> entry : stateNumMap.entrySet()) {
//            result.add(new OrderNumOfOneState(entry.getKey(), entry.getValue()));
//        }
//        return result;
//    }
//
//    @Override
//    public List<NumOfOneMemberLevel> checkMemberLevels() throws UserNotExistException {
//        // 初始化对应的 map
//        Map<Integer, Integer> levelNumMap = new HashMap<>();
//        for (int i = 1; i <= 8; i++) {
//            levelNumMap.put(i, 0);
//        }
//
//        for (User cur : userDao.getAllUser(UserType.MEMBER)) {
//            final int curLevel = ((Member) cur).getLevel();
//            int num = levelNumMap.get(curLevel);
//            num++;
//            levelNumMap.put(curLevel, num);
//        }
//
//        List<NumOfOneMemberLevel> result = new LinkedList<>();
//        for (Map.Entry<Integer, Integer> entry : levelNumMap.entrySet()) {
//            result.add(new NumOfOneMemberLevel(entry.getKey(), entry.getValue()));
//        }
//        return result;
//    }
//
//    @Override
//    public List<OrderNumOfOneState> checkMemberOrders() {
//        // 初始化对应的 map
//        Map<OrderState, Integer> stateNumMap = new HashMap<>();
//        for (OrderState state : OrderState.values()) {
//            stateNumMap.put(state, 0);
//        }
//
//        for (Order cur : orderDao.getAllOrders()) {
//            OrderState curState = cur.getOrderState();
//            int num = stateNumMap.get(curState);
//            num++;
//            stateNumMap.put(curState, num);
//        }
//
//        List<OrderNumOfOneState> result = new LinkedList<>();
//        for (Map.Entry<OrderState, Integer> entry : stateNumMap.entrySet()) {
//            result.add(new OrderNumOfOneState(entry.getKey(), entry.getValue()));
//        }
//        return result;
//    }
//
//    @Override
//    public List<IncomeOfOneSpot> checkSpotsIncome() throws UserNotExistException {
//        Map<String, String> spotIdNameMap = new HashMap<>();
//        Map<String, List<Schedule>> spotSchedulesMap = new HashMap<>();
//
//        // 初始化对应的 map
//        for (User curUser : userDao.getAllUser(UserType.SPOT)) {
//            Spot curSpot = (Spot) curUser;
//            spotIdNameMap.put(curSpot.getId(), curSpot.getSpotName());
//            spotSchedulesMap.put(curSpot.getId(), new LinkedList<>());
//        }
//
//        // 将计划按场馆分类
//        for (Schedule cur : scheduleDao.getAllSchedules()) {
//            List<Schedule> already = spotSchedulesMap.get(cur.getSpot().getId());
//            already.add(cur);
//            spotSchedulesMap.put(cur.getSpot().getId(), already);
//        }
//
//        List<IncomeOfOneSpot> result = new LinkedList<>();
//        for (Map.Entry<String, List<Schedule>> entry : spotSchedulesMap.entrySet()) {
//            double income = 0;
//            for (Schedule cur : entry.getValue()) {
//                income += cur.getBalance();
//            }
//            result.add(new IncomeOfOneSpot(spotIdNameMap.get(entry.getKey()), (int) income));
//        }
//        return result;
//    }
//}
