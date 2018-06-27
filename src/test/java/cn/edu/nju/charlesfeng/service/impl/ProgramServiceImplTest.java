package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.filter.program.PreviewSearchResult;
import cn.edu.nju.charlesfeng.util.filter.program.ProgramBrief;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgramServiceImplTest {

    @Autowired
    private ProgramService programService;

    @Autowired
    private TicketService ticketService;

    @Test
    public void recommendPrograms() {
        long start = System.currentTimeMillis();
        Map<String, List<Program>> map = programService.recommendPrograms(LocalDateTime.now(), "南京", 5);
        Map<String, List<ProgramBrief>> result = new HashMap<>();
        for (String key : map.keySet()) {
            List<Program> programs = map.get(key);
            List<ProgramBrief> programBriefs = new ArrayList<>();
            for (Program program : programs) {
                programBriefs.add(new ProgramBrief(program));
            }
            result.put(key, programBriefs);
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("--------------------------------");
        long start1 = System.currentTimeMillis();
        Map<String, List<Program>> map1 = programService.recommendPrograms(LocalDateTime.now(), "上海", 5);
        Map<String, List<ProgramBrief>> result1 = new HashMap<>();
        for (String key : map1.keySet()) {
            List<Program> programs = map1.get(key);
            List<ProgramBrief> programBriefs = new ArrayList<>();
            for (Program program : programs) {
                programBriefs.add(new ProgramBrief(program));
            }
            result1.put(key, programBriefs);
        }
        System.out.println(System.currentTimeMillis() - start1);
    }

    @Test
    public void test() {
        Map<String, List<Program>> map = programService.recommendPrograms(LocalDateTime.now(), "南京", 5);
        Map<String, List<ProgramBrief>> result = new HashMap<>();
        for (String key : map.keySet()) {
            List<Program> programs = map.get(key);
            List<ProgramBrief> programBriefs = new ArrayList<>();
            for (Program program : programs) {
                programBriefs.add(new ProgramBrief(program));
            }
            result.put(key, programBriefs);
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

    @Test
    public void getBriefPrograms() {
    }

    @Test
    public void getAllProgramField() {
    }

    @Test
    public void search() {
        String conditions = "2";
        Set<Program> programs = programService.search(conditions);
        System.out.println("-------------------------");
    }

    @Test
    public void previewSearch() {
        List<PreviewSearchResult> result = programService.previewSearch("上海 张韶涵", 10);
        System.out.println("-------------------------");
    }

    @Test
    public void testProgramBrief() {

        String id = "129;2018-08-11T18:35";
        String id1 = "129;2018-08-15T17:00";
        long start = System.currentTimeMillis();

        String fast_id = "120;2018-06-29T00:00";
        String ids[] = fast_id.split(";");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(ids[0]));
        programID.setStartTime(LocalDateTime.parse(ids[1]));
        Program program = programService.getOneProgram(programID);
        SaleType saleType = ticketService.getProgramSaleType(programID);
        Set<LocalDateTime> fields = programService.getAllProgramField(programID.getVenueID(), program.getName());
        int number = ticketService.getProgramRemainTicketNumber(programID);
        System.out.println(System.currentTimeMillis() - start);
    }
}