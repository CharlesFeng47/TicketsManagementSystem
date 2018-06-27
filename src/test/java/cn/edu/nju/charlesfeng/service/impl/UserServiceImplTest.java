package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void star() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(15);
        programID.setStartTime(LocalDateTime.of(2017, 6, 14, 0, 0, 0));

        int num = userService.star(programID, "151250032@smail.nju.edu.cn");
        System.out.println(num);
    }

    @Test
    public void cancelStar() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(15);
        programID.setStartTime(LocalDateTime.of(2017, 6, 14, 0, 0, 0));
        int num = userService.cancelStar(programID, "151250032@smail.nju.edu.cn");
        System.out.println(num);
    }
}