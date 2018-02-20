package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper.scheduleDao;

/**
 * ScheduleDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 20, 2018</pre>
 */
public class ScheduleDaoImplTest {

    private UserDao userDao;

    @Before
    public void before() throws Exception {
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
//TODO: Test goes here... 
    }

    /**
     * Method: saveSchedule(Schedule schedule)
     */
    @Test
    public void testSaveSchedule() throws Exception {
        Map<SeatInfo, Double> seatPrices = new HashMap<>();

        Spot spot = (Spot) userDao.getUser("重庆江北大剧院", UserType.SPOT);
        Set<SeatInfo> seats = spot.getSeatInfos();
        int i = 1;
        for (SeatInfo seat : seats) {
            seatPrices.put(seat, (double) (i * 200));
            i++;
        }

        Schedule schedule = new Schedule(LocalDateTime.now(), ScheduleItemType.CONCERT, seatPrices, "测试用日程");
        scheduleDao.saveSchedule(schedule);
    }

    /**
     * Method: updateSchedule(Schedule schedule)
     */
    @Test
    public void testUpdateSchedule() throws Exception {
        Map<SeatInfo, Double> seatPrices = new HashMap<>();

        Spot spot = (Spot) userDao.getUser("重庆江北大剧院", UserType.SPOT);
        Set<SeatInfo> seats = spot.getSeatInfos();
        int i = 1;
        for (SeatInfo seat : seats) {
            if (seat.getId() == 2) seatPrices.put(seat, (double) 300);
            else seatPrices.put(seat, (double) (i * 200));
            i++;
        }

        Schedule schedule = new Schedule(LocalDateTime.now(), ScheduleItemType.CONCERT, seatPrices, "测试用日程");
        scheduleDao.updateSchedule(schedule);
    }

    /**
     * Method: deleteSchedule(Schedule schedule)
     */
    @Test
    public void testDeleteSchedule() throws Exception {
        Schedule schedule = scheduleDao.getSchedule(0);
        scheduleDao.deleteSchedule(schedule);
    }


} 
