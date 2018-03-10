package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Manager;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * UserDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 15, 2018</pre>
 */
public class UserDaoImplTest {

    private Logger logger = Logger.getLogger(UserDaoImplTest.class);

    private UserDao dao;

    @Before
    public void before() throws Exception {
        dao = DaoTestHelper.userDao;
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetMember1() throws Exception {
        Member member = (Member) dao.getUser("suzy", UserType.MEMBER);
        logger.debug(member);
    }

    @Test(expected = UserNotExistException.class)
    public void testGetMember2() throws Exception {
        Member member = (Member) dao.getUser("daisy", UserType.MEMBER);
        logger.debug(member);
    }

    @Test
    public void testGetManager() throws Exception {
        Manager adminManager = (Manager) dao.getUser("admin", UserType.MANAGER);
        logger.debug(adminManager);
    }

    @Test
    public void testGetSpot() throws Exception {
        Spot spot = (Spot) dao.getUser("重庆江北大剧院", UserType.SPOT);
        logger.debug(spot);
    }

    @Test
    public void testManager() throws Exception {
        Manager manager1 = new Manager("cuihua", "qwertyuiop123456");
        String saveResult = dao.saveUser(manager1, UserType.MANAGER);
        logger.debug(saveResult);

        Manager manager2 = new Manager("cuihua", "qwertyuiop");
        boolean updateResult = dao.updateUser(manager2, UserType.MANAGER);
        logger.debug(updateResult);

        Manager resultManager = (Manager) dao.getUser("cuihua", UserType.MANAGER);
        logger.debug(resultManager);
    }

    @Test
    public void testMember() throws Exception {
        Member member1 = new Member("suzy", "qwertyuiop123456", "12345@126.com");
        String saveResult = dao.saveUser(member1, UserType.MEMBER);
        logger.debug(saveResult);

        Member member2 = new Member("suzy", "qwertyuiop", "1234567890@126.com");
        boolean updateResult = dao.updateUser(member2, UserType.MEMBER);
        logger.debug(updateResult);

        Member resultMember = (Member) dao.getUser("suzy", UserType.MEMBER);
        logger.debug(resultMember);
    }

    @Test
    public void testSpot() throws Exception {
        SeatInfo seat1 = new SeatInfo("s1", "一等座", 100);
        SeatInfo seat2 = new SeatInfo("s2", "二等座", 100);
        SeatInfo seat3 = new SeatInfo("s3", "三等座", 100);
        List<SeatInfo> seatInfos = new LinkedList<>();
        seatInfos.add(seat1);
        seatInfos.add(seat2);
        seatInfos.add(seat3);

        String allSeatsJson = "[\n" +
                "          'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',\n" +
                "          'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb',\n" +
                "          'cccccccccccccccccccccccccccccccccccccccccccccccccc',\n" +
                "          'dddddddddddddddddddddddddddddddddddddddddddddddddd',\n" +
                "          'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',\n" +
                "          'ffffffffffffffffffffffffffffffffffffffffffffffffff',\n" +
                "          'gggggggggggggggggggggggggggggggggggggggggggggggggg',\n" +
                "          'hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh',\n" +
                "          'iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii'\n" +
                "        ]";

        Spot spot1 = new Spot("0000001", "qwertyuiop", "重庆江北大剧院", false, "重庆江北嘴", seatInfos, allSeatsJson, 2);
        String saveResult = dao.saveUser(spot1, UserType.SPOT);
        logger.debug(saveResult);

        SeatInfo seat2new = new SeatInfo("s2", "二等座", 150);
        seatInfos.remove(seat2);
        seatInfos.add(seat2new);
        Spot spot2 = new Spot("0000001", "qwertyuiop", "重庆江北大剧院", false, "重庆江北嘴", seatInfos, allSeatsJson, 2);
        boolean updateResult = dao.updateUser(spot2, UserType.SPOT);
        logger.debug(updateResult);

        Spot resultSpot = (Spot) dao.getUser("0000001", UserType.SPOT);
        logger.debug(resultSpot);
    }

    @Test
    public void testSpot2() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append("s").append(now.getMonthValue()).append(now.getDayOfMonth());
        sb.append(now.getHour()).append(now.getMinute()).append(now.getSecond());
        String sid = sb.toString();
        SeatInfo seat1 = new SeatInfo(sid + "/1", "金座", 200);
        SeatInfo seat2 = new SeatInfo(sid + "/2", "银座", 400);
        SeatInfo seat3 = new SeatInfo(sid + "/3", "铜座", 1000);
        List<SeatInfo> seatInfos = new LinkedList<>();
        seatInfos.add(seat1);
        seatInfos.add(seat2);
        seatInfos.add(seat3);

        List<Seat> allSeats = new LinkedList<>();
        allSeats.add(new Seat(1, 1));
        allSeats.add(new Seat(1, 2));
        allSeats.add(new Seat(1, 3));
        allSeats.add(new Seat(1, 4));
        allSeats.add(new Seat(2, 1));
        allSeats.add(new Seat(2, 2));
        allSeats.add(new Seat(2, 3));
        allSeats.add(new Seat(2, 4));
        String allSeatsJson = JSON.toJSONString(allSeats);

        Spot spot1 = new Spot("0000002", "qwertyuiop", "南京江苏大剧院", false, "南京河西新区", seatInfos, allSeatsJson, 2);
        String saveResult = dao.saveUser(spot1, UserType.SPOT);
        logger.debug(saveResult);

        Spot resultSpot = (Spot) dao.getUser("0000001", UserType.SPOT);
        logger.debug(resultSpot);
    }

    @Test
    public void testGetAllUser() throws UserNotExistException {
        List<User> members = dao.getAllUser(UserType.MEMBER);
        showUserId(members);

        List<User> spots = dao.getAllUser(UserType.SPOT);
        showUserId(spots);

        List<User> managers = dao.getAllUser(UserType.MANAGER);
        showUserId(managers);
    }

    private void showUserId(List<User> users) {
        for (User user : users) {
            logger.debug(user.getId());
        }
    }
}
