//package cn.edu.nju.charlesfeng.task;
//
//import cn.edu.nju.charlesfeng.dao.UserRepository;
//import cn.edu.nju.charlesfeng.model.Member;
//import cn.edu.nju.charlesfeng.filter.User;
//import cn.edu.nju.charlesfeng.util.enums.UserType;
//import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 会员积分涨了之后，修改会员等级
// */
//@Component
//public class MemberTask {
//
//    private final static Logger logger = Logger.getLogger(ScheduleTask.class);
//
//    private final UserRepository userDao;
//
//    @Autowired
//    public MemberTask(UserRepository userDao) {
//        this.userDao = userDao;
//    }
//
//    /**
//     * 每分钟检查会员等级
//     */
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void MemberLevelAutoUp() throws UserNotExistException {
//        logger.info("MemberLevelAutoUp Task 开始工作");
//
//        final int[] level = {100, 500, 1000, 2000, 4000, 8000, 15000, 40000};
//        for (User cur : userDao.getAllUser(UserType.MEMBER)) {
//            Member curMember = (Member) cur;
//
//            int curLevel = curMember.getLevel();
//            if (curMember.getCreditTotal() > level[curLevel - 1]) {
//                curLevel++;
//                curMember.setLevel(curLevel);
//                userDao.updateUser(curMember, UserType.MEMBER);
//            }
//        }
//    }
//
//}
