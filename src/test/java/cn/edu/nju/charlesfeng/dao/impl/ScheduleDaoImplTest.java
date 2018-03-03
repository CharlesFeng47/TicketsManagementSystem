package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ScheduleDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 20, 2018</pre>
 */
public class ScheduleDaoImplTest {

    private ScheduleDao scheduleDao;
    private UserDao userDao;

    @Before
    public void before() throws Exception {
        scheduleDao = DaoTestHelper.scheduleDao;
        userDao = DaoTestHelper.userDao;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getSchedule(int scheduleId)
     */
    @Test
    public void testGetSchedule() throws Exception {
        Schedule schedule = scheduleDao.getSchedule("2018330342");
        String json = JSON.toJSONString(schedule);
        System.out.println(json);
        Schedule another = JSON.parseObject(json, Schedule.class);
        System.out.println(another.getId());
    }

    @Test
    public void testGetAllSchedules() throws Exception {
        List<Schedule> allSchedules = scheduleDao.getAllSchedules();
        for (Schedule schedule : allSchedules) {
            System.out.println("scheduleId: " + schedule.getId());
        }
    }

    /**
     * Method: saveSchedule(Schedule schedule)
     */
    @Test
    public void testSaveSchedule() throws Exception {
        Map<SeatInfo, Double> seatPrices = new HashMap<>();

        Spot spot = (Spot) userDao.getUser("0000001", UserType.SPOT);
        Set<SeatInfo> seats = spot.getSeatInfos();
        int i = 1;
        for (SeatInfo seat : seats) {
            seatPrices.put(seat, (double) (i * 400));
            i++;
        }

        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(now.getYear()).append(now.getMonthValue()).append(now.getDayOfMonth());
        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
        Schedule schedule = new Schedule(sb.toString(), "测试用日程名字2", "0000001", LocalDateTime.now(), ScheduleItemType.CONCERT, seatPrices, "测试用日程描述");
        scheduleDao.saveSchedule(schedule);
    }

    /**
     * Method: updateSchedule(Schedule schedule)
     * TODO
     */
    @Test
    public void testUpdateSchedule() throws Exception {
        Schedule schedule = scheduleDao.getSchedule("2018330342");
        Map<SeatInfo, Double> map = schedule.getSeatPrices();
        for (Map.Entry<SeatInfo, Double> entry : map.entrySet()) {
            if (entry.getKey().getId() == "s2") entry.setValue((double) 500);
        }
        scheduleDao.updateSchedule(schedule);
    }

    /**
     * Method: deleteSchedule(Schedule schedule)
     */
    @Test
    public void testDeleteSchedule() throws Exception {
        scheduleDao.deleteSchedule("0");
    }


}
