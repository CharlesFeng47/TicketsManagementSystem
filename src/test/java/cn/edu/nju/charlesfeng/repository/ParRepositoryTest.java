package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ParID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import io.swagger.models.auth.In;
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

    @Autowired
    private SeatRepository seatRepository;

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
//    @Rollback
//    @Transactional
    public void testModify() {
        List<Par> pars = parRepository.findAll();
        Set<Par> last_pars = new HashSet();
        Set<Par> new_pars = new HashSet();
        for (Par par : pars) {
            String price = String.valueOf(par.getParID().getBasePrice());
            if (price.endsWith(".99")) {
                Par new_par = new Par();
                double basePrice = par.getParID().getBasePrice() - 0.99;
                ParID new_parID = new ParID();
                ParID parID = par.getParID();
                new_parID.setProgramID(parID.getProgramID());
                new_parID.setComments(parID.getComments());
                new_parID.setBasePrice(Math.round(basePrice + randomPrice()));
                new_par.setParID(new_parID);
                new_par.setProgram(par.getProgram());
                new_par.setSeatType(par.getSeatType());
                new_par.setDiscount(par.getDiscount());
                new_pars.add(new_par);

                par.setProgram(null);
                last_pars.add(par);
            }
        }

        System.out.println("去除外键的限制");
        parRepository.saveAll(last_pars);
        System.out.println("新增记录");
        parRepository.saveAll(new_pars);
        System.out.println("删除无用记录");
        parRepository.deleteAll(last_pars);
    }

    @Test
    public void testGet() {

    }

    @Test
    public void testDelete() {
    }


    @Test
    //@Rollback
    public void testOtherType() {
        List<Program> programs = programRepository.findAll();
        for (Program program : programs) {
            System.out.println("----------------------------------------------------------");
            System.out.println("当前更新节目：" + program.getName());
            Venue venue = program.getVenue();
            List<String> types = seatRepository.getType(venue.getVenueID());
            Set<Par> pars = program.getPars();
            if (pars.size() > types.size()) {
                //票面种类多于座位
                System.out.println("票面种类多于座位");
                Set<Par> newPars = new HashSet<>();
                //Set<Par> deletePars = new HashSet<>();
                int counter = 0;
                for (Par par : pars) {
                    if (counter == types.size()) {
                        //deletePars.add(par);
                        par.setProgram(null);
                        parRepository.save(par);
                        parRepository.delete(par);
                        System.out.println("delete:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                    } else {
                        par.setSeatType(types.get(counter));
                        newPars.add(par);
                        counter = counter + 1;
                        System.out.println("modify:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                    }
                }

//                program.setPars(newPars);
//                programRepository.save(program);
                parRepository.saveAll(newPars);
                //pars.clear();
                //parRepository.deleteAll(deletePars);
            }
        }
    }

    @Test
    //@Rollback
    public void testType() {
        List<Program> programs = programRepository.findAll();
        List<Program> no_venue = new ArrayList<>();
        Set<Par> need_delete = new HashSet<>();
        for (Program program : programs) {
            System.out.println("----------------------------------------------------------");
            System.out.println("当前更新节目：" + program.getName());
            Venue venue = program.getVenue();
            if (venue == null) {
                no_venue.add(program);
                System.out.println("该节目未绑定场馆");
                continue;
            }
            List<String> types = seatRepository.getType(venue.getVenueID());
            Set<Par> pars = program.getPars();
            if (pars.isEmpty()) {
                //该节目没有成功添加票面，需要根据所谓类型添加
                System.out.println("该节目没有成功添加票面，需要根据所谓类型添加");
                double basePrice = 100.99;
                for (String type : types) {
                    ParID parID = new ParID();
                    parID.setProgramID(program.getProgramID());
                    parID.setBasePrice(basePrice + (types.indexOf(type) * 100));
                    parID.setComments("优惠多多");
                    Par par = new Par();
                    par.setParID(parID);
                    par.setProgram(program);
                    par.setDiscount(1);
                    par.setSeatType(type);
                    pars.add(par);
                    System.out.println("add:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                }
                parRepository.saveAll(pars);
                program.setPars(pars);
                programRepository.save(program);
            } else if (pars.size() > types.size()) {
                //票面种类多于座位
                System.out.println("票面种类多于座位--暂时不做");
//                Set<Par> newPars = new HashSet<>();
//                Set<Par> deletePars = new HashSet<>();
//                int counter = 0;
//                for (Par par : pars) {
//                    if (counter == types.size()) {
//                        deletePars.add(par);
//                        System.out.println("delete:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
//                    } else {
//                        par.setSeatType(types.get(counter));
//                        newPars.add(par);
//                        counter = counter + 1;
//                        System.out.println("modify:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
//                    }
//                }
//
//                program.setPars(newPars);
//                programRepository.save(program);
//                parRepository.saveAll(newPars);
//                parRepository.deleteAll(deletePars);

            } else if (types.size() > pars.size()) {
                //座位种类多于票面
                System.out.println("座位种类多于票面");
                int counter = 0;
                for (Par par : pars) {
                    par.setSeatType(types.get(counter));
                    counter = counter + 1;
                    System.out.println("modify:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                }

                double basePrice = 100.99;
                for (int i = counter; i < types.size(); i++) {
                    ParID parID = new ParID();
                    parID.setProgramID(program.getProgramID());
                    parID.setBasePrice(basePrice + (i * 100));
                    parID.setComments("优惠多多");
                    Par par = new Par();
                    par.setParID(parID);
                    par.setProgram(program);
                    par.setDiscount(1);
                    par.setSeatType(types.get(i));
                    pars.add(par);
                    System.out.println("add:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                }

                parRepository.saveAll(pars);
                program.setPars(pars);
                programRepository.save(program);
            } else {
                //两个种类数相同
                System.out.println("两个种类数相同");
                int counter = 0;
                for (Par par : pars) {
                    par.setSeatType(types.get(counter));
                    counter++;
                    System.out.println("modify:" + par.getParID().getProgramID().getVenueID() + "--" + par.getParID().getBasePrice() + "--" + par.getSeatType());
                }
                parRepository.saveAll(pars);
                program.setPars(pars);
                programRepository.save(program);
            }
        }
        writerPassed(no_venue);
    }

    @Test
    public void testAddAll() {
        List<String> passed = new ArrayList<>();
        Map<String, List<Map<String, String>>> pars = readPar();
        for (String key : pars.keySet()) {
            System.out.println("------------------------------------------------");
            System.out.println("key:" + key);
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

    @Test
    public void testAddPar() {
        List<ProgramID> programIDS = readUnGetProgram();
        for (ProgramID programID : programIDS) {
            Program program = programRepository.findByProgramID(programID);
            List<String> types = seatRepository.getType(programID.getVenueID());
            List<Par> result = new ArrayList<>();
            for (String type : types) {
                ParID parID = new ParID();
                parID.setProgramID(programID);
                parID.setBasePrice(randomPrice());
                parID.setComments("优惠多多");
                Par par = new Par();
                par.setParID(parID);
                par.setDiscount(1);
                par.setSeatType(type);
                par.setProgram(program);
                result.add(par);
                System.out.println(par.getSeatType()+"-"+par.getParID().getProgramID().getVenueID()+"-"+par.getParID().getProgramID().getStartTime().toString()+"-"+par.getParID().getBasePrice());
            }
            parRepository.saveAll(result);
        }
    }

    public List<ProgramID> readUnGetProgram() {
        String path = "C:\\Users\\Byron Dong\\Desktop\\2.txt";
        File file = new File(path);
        List<ProgramID> programIDS = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String info[] = line.split(";");
                ProgramID programID = new ProgramID();
                programID.setVenueID(Integer.parseInt(info[0]));
                programID.setStartTime(LocalDateTime.parse(info[1]));
                programIDS.add(programID);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return programIDS;
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

    private void writerPassed(List<Program> list) {
        File file = new File("C:\\Users\\Byron Dong\\Desktop\\1.txt");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (Program info : list) {
                bufferedWriter.write(info.getProgramID().getVenueID() + "--" + info.getProgramID().getStartTime().toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int randomPrice() {
        return ((int) (Math.random() * 900)) + 100;
    }
}