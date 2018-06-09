package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgramServiceImplTest {

    @Autowired
    private ProgramService programService;

    @Test
    public void recommendPrograms() {
        Map<ProgramType, List<Program>> result = programService.recommendPrograms(LocalDateTime.now(),"南京",5);
        for (ProgramType type : result.keySet()) {
            List<Program> programs = result.get(type);
            System.out.println(type);
            for (Program program : programs) {
                System.out.println(program.getName());
            }
            System.out.println("-----------------------------------------------------");
        }
    }

    @Test
    public void getAllPrograms() {
    }

    @Test
    public void getAllAvailablePrograms() {
    }

    @Test
    public void getProgramsOfOneVenue() {
    }

    @Test
    public void getOneProgram() {
    }

    @Test
    public void publishProgram() {
    }

    @Test
    public void modifyProgram() {
    }

    @Test
    public void settleOneProgram() {
    }
}