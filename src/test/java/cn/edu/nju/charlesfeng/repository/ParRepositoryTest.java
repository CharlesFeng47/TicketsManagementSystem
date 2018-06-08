package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ParID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ParRepository parRepository;

    @Test
    public void testAdd() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        Program program = programRepository.getOne(programID);

        Set<Par> pars = new HashSet<>();
        Par par1 = new Par();
        ParID parID1 = new ParID();
        parID1.setProgramID(programID);
        parID1.setBasePrice(200);
        par1.setParID(parID1);
        par1.setProgram(program);
        par1.setSeatType("看台");
        par1.setDiscount(0.9);
//        par1.setComments("优惠");
        parRepository.save(par1);
        pars.add(par1);

        Par par2 = new Par();
        ParID parID2 = new ParID();
        parID2.setProgramID(programID);
        parID2.setBasePrice(400);
        par2.setParID(parID2);
        par2.setProgram(program);
        par2.setSeatType("后台");
        par2.setDiscount(0.9);
//        par2.setComments("优惠");
        parRepository.save(par2);
        pars.add(par2);

        program.setPars(pars);
        programRepository.save(program);
    }

    @Test
    public void testModify() {

    }

    @Test
    public void testGet() {

    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testAddAll() {
        List<String> passed = new ArrayList<>();
        Map<String, List<Map<String, String>>> pars = readPar();
        for (String key : pars.keySet()) {
            System.out.println("------------------------------------------------");
            System.out.println("key:"+key);
            if (passed.contains(key)) {
                continue;
            }

            List<Program> programs = programRepository.findByName(key);
            if (programs == null || programs.isEmpty()) {
                continue;
            }

            Program program = null;
            if (programs.size() > 1) {
                for (Program program1 : programs) {
                    if (program1.getPars() == null || program1.getPars().isEmpty()) {
                        program = program1;
                    }
                }
            } else {
                program = programs.get(0);
            }

            if (program == null) {
                continue;
            }

            List<Map<String, String>> list = pars.get(key);
            Iterable<Par> pars1 = new HashSet<>();
            for (Map<String, String> item : list) {
                System.out.println("----element-------");
                ParID parID = new ParID();
                parID.setProgramID(program.getProgramID());
                parID.setBasePrice(Integer.parseInt(item.get("price").replace("元", "")));
                parID.setComments(item.get("comments"));
                Par par = new Par();
                par.setParID(parID);
                par.setDiscount(Double.parseDouble(item.get("discount")));
                par.setProgram(program);
                par.setSeatType("");
                ((HashSet<Par>) pars1).add(par);
                System.out.println(par.getParID().getComments() + "  " + par.getDiscount() + " " + par.getProgram().getName());
                System.out.println("----element end---");
                try {
                    parRepository.save(par);
                } catch (Exception e) {
                    System.out.println("出现键值重复" + par.getProgram().getName());
                }
            }

            program.setPars((Set<Par>) pars1);
            programRepository.save(program);
            System.out.println("------------------------------------------------");
        }


    }

    public Map<String, List<Map<String, String>>> readPar() {
        File file = new File("F:\\crawler\\detail.txt");
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                List<Map<String, String>> program = new ArrayList<>();
                String programName = bufferedReader.readLine();
                line = bufferedReader.readLine(); //just read first line for loop
                while (!line.startsWith("E----")) {
                    Map<String, String> element = new HashMap<>();
                    String comments = line; //comments;
                    String price = bufferedReader.readLine(); //price
                    String discount = bufferedReader.readLine(); //discount
                    line = bufferedReader.readLine(); //may be E---- or comments

                    String prices[] = price.split(":");
                    if (prices.length == 1 || prices[1].equals("0元")) {
                        continue;
                    } else {
                        element.put(prices[0], prices[1]);
                    }

                    String comment[] = comments.split(":");
                    if (comment.length == 1) {
                        element.put("comments", "");
                    } else {
                        element.put(comment[0], comment[1]);
                    }

                    String discounts[] = discount.split(":");
                    element.put(discounts[0], discounts[1]);
                    program.add(element);
                }
                result.put(programName, program);
            }
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void writerPassed(List<String> list) {
        File file = new File("C:\\Users\\Byron Dong\\Desktop\\1.txt");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (String info : list) {
                bufferedWriter.write(info);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}