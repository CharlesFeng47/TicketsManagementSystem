package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.helper.ImageHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.channels.FileChannel;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    public void testAdd() {
        Program program = new Program();
        Venue venue = venueRepository.getOne(1);
        program.setVenue(venue);
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        program.setProgramID(programID);
        program.setDescription("good");
        program.setFavoriteVolume(200);
        program.setName("YIYA NO");
        program.setProgramType(ProgramType.CONCERT);
        program.setScanVolume(23456);
//        program.setPoster(getBaseImg());
        programRepository.save(program);
    }

    @Test
    public void testModify() {

    }

    @Test
    public void testGet() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        Program program = programRepository.getOne(programID);
        System.out.println(program.getVenue().getVenueName());
        System.out.println(program.getPars().size());

    }

    @Test
    public void testDelete() {
    }

    @Test
//    @Rollback
//    @Transactional
    public void testAddAll() {
        Map<String, ProgramType> map = new HashMap<>();
        List<String> programsInfo = new ArrayList<>();
        map.put("vocalconcert", ProgramType.VOCALCONCERT);
        map.put("concert", ProgramType.CONCERT);
        map.put("ballet", ProgramType.DANCE);
        map.put("drama", ProgramType.DRAMA);
        map.put("exhibition", ProgramType.EXHIBITION);
        map.put("child", ProgramType.PARENTCHILD);
        map.put("match", ProgramType.SPORT);
        map.put("quyi", ProgramType.QUYITALK);

        String root = "F:\\new_crawler\\images\\";
        for (String folder : map.keySet()) {
            String path = "F:\\new_crawler\\" + folder + "\\";
            File dir = new File(path);
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                String filePath = file.getPath();
                if (filePath.endsWith(".txt")) {
                    String programName = filePath.substring(filePath.lastIndexOf("\\") + 1).replace(".txt", "");
                    String name = readName2(filePath);
                    Venue venue = venueRepository.findByVenueName(name);
                    Map<String, String> content = readContent(filePath);
                    String time = content.get("time");
                    System.out.println(name);
                    if (time.contains("-")) { //存在多个场次
                        String times[] = time.split("-");
                        LocalDate start = LocalDate.parse(times[0].replace(".", "-"));
                        LocalDate end = LocalDate.parse(times[1].replace(".", "-"));

                        long distance = ChronoUnit.DAYS.between(start, end);
                        Stream.iterate(start, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> {
                            if (!f.isBefore(LocalDate.of(2018, 7, 9))) {
                                ProgramID programID = new ProgramID();
                                programID.setVenueID(venue.getVenueID());
                                programID.setStartTime(LocalDateTime.of(f, LocalTime.of(0, 0, 0))); //设置时间
                                Program program = new Program();
                                program.setProgramID(programID);
                                program.setVenue(venue);
                                program.setName(programName);
                                program.setDescription(content.get("desc"));
                                program.setProgramType(map.get(folder));
                                program.setScanVolume(Integer.parseInt(content.get("scan").replace("人浏览", "")));
                                program.setFavoriteVolume(Integer.parseInt(content.get("like").replace("人想看", "")));
                                venue.getPrograms().add(program);
                                System.out.println(programID.getVenueID() + ";" + programID.getStartTime().toString() + ";" + program.getName() + ";" + program.getProgramType().toString());
                                Program testProgram = programRepository.findByProgramID(programID);
                                if (testProgram != null) {
                                    System.out.println("重复：" + programID.getVenueID() + "--" + programID.getStartTime().toString());
                                } else {
                                    programsInfo.add(programID.getVenueID() + ";" + programID.getStartTime().toString() + ";" + program.getName() + ";" + program.getProgramType().toString());
                                    String imagePath = filePath.replace(".txt", ".jpg");
                                    String realPath = root + program.getProgramType().name() + "\\" + String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime())) + ".jpg";
                                    System.out.println(imagePath);
                                    copyFile(imagePath, realPath);
                                    programRepository.save(program);
                                }
                            }
                        });
                    } else {
                        time = time.replace(".", "-");
                        time = time.replace(' ', 'T');
                        time = time + ":00";
                        LocalDateTime dateTime = LocalDateTime.parse(time);
                        if (!dateTime.isBefore(LocalDateTime.of(2018, 7, 9, 0, 0, 0))) {
                            ProgramID programID = new ProgramID();
                            programID.setVenueID(venue.getVenueID());
                            programID.setStartTime(dateTime); //设置时间
                            Program program = new Program();
                            program.setProgramID(programID);
                            program.setVenue(venue);
                            program.setName(programName);
                            program.setDescription(content.get("desc"));
                            program.setProgramType(map.get(folder));
                            program.setScanVolume(Integer.parseInt(content.get("scan").replace("人浏览", "")));
                            program.setFavoriteVolume(Integer.parseInt(content.get("like").replace("人想看", "")));
                            venue.getPrograms().add(program);
                            System.out.println(programID.getVenueID() + ";" + programID.getStartTime().toString() + ";" + program.getName() + ";" + program.getProgramType().toString());
                            Program testProgram = programRepository.findByProgramID(programID);
                            if (testProgram != null) {
                                System.out.println("重复：" + programID.getVenueID() + "--" + programID.getStartTime().toString());
                            } else {
                                programsInfo.add(programID.getVenueID() + ";" + programID.getStartTime().toString() + ";" + program.getName() + ";" + program.getProgramType().toString());
                                String imagePath = filePath.replace(".txt", ".jpg");
                                String realPath = root + program.getProgramType().name() + "\\" + String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime())) + ".jpg";
                                System.out.println(imagePath);
                                copyFile(imagePath, realPath);
                                programRepository.save(program);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(programsInfo.size());
        writerProgramInfo(programsInfo);
    }

    private Map<String, String> readContent(String path) {
        File file = new File(path);
        Map<String, String> result = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("comment")) {
                    break;
                }
                if (line.startsWith("time")) {
                    String time = line.substring(line.indexOf(":") + 1);
                    if (time.startsWith("时间待定")) {
                        time = LocalDateTime.of(2018, 7, 15, 18, 30, 0).toString();
                    }
                    result.put("time", time);
                    continue;
                }

                String temp[] = line.split(":");
                if (temp.length == 1) {
                    result.put(temp[0], "");
                } else {
                    result.put(temp[0], temp[1]);
                }

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

    private String readName(String path) {
        String name = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            while ((name = bufferedReader.readLine()) != null) {
                if (name.startsWith("address")) {
                    break;
                }
            }
            bufferedReader.close();

            name = name.split(":")[1];
            if (!name.startsWith("上汽") && !name.startsWith("「上生·新所」")) {
                if (name.contains("-")) {
                    name = name.substring(0, name.indexOf("-"));
                }

                if (name.contains("—")) {
                    name = name.substring(0, name.indexOf("—"));
                }

                if (name.contains("——")) {
                    name = name.substring(0, name.indexOf("——"));
                }

                if (name.contains("·")) {
                    name = name.substring(0, name.indexOf("·"));
                }
            }

            if (name.startsWith("上海梅赛德斯")) {
                name = "上海梅赛德斯";
            }

            if (name.startsWith("无锡大剧院")) {
                name = "无锡大剧院";
            }

            if (name.startsWith("南京保利大剧院")) {
                name = "南京保利大剧院";
            }

            if (name.startsWith("上海交响乐团")) {
                name = "上海交响乐团音乐厅";
            }
            return name;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String readName2(String path) {
        String name = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            while ((name = bufferedReader.readLine()) != null) {
                if (name.startsWith("address")) {
                    break;
                }
            }
            bufferedReader.close();
            name = name.split(":")[1];
//            if (!name.startsWith("上汽") && !name.startsWith("「上生·新所」")) {
//                if (name.contains("-")) {
//                    name = name.substring(0, name.indexOf("-"));
//                }
//
//                if (name.contains("—")) {
//                    name = name.substring(0, name.indexOf("—"));
//                }
//
//                if (name.contains("——")) {
//                    name = name.substring(0, name.indexOf("——"));
//                }
//
//                if (name.contains("·")) {
//                    name = name.substring(0, name.indexOf("·"));
//                }
//            }
//
//
            if (name.startsWith("南京青奥体育公园")) {
                name = "南京青奥体育公园体育馆";
            }

            if (name.startsWith("江苏大剧院")) {
                name = "江苏大剧院";
            }

            if (name.startsWith("无锡大剧院")) {
                name = "无锡大剧院";
            }

            if (name.startsWith("南京保利大剧院")) {
                name = "南京保利大剧院";
            }
            return name;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private void copyFile(String sourcePath, String destPath) {
        ImageHelper.createImg(ImageHelper.getBaseImg(sourcePath).replace("data:image/jpeg;base64,", ""), destPath);
    }

    private void writerErrorName(Map<String, String> names) {
        String path = "C:\\Users\\Byron Dong\\Desktop\\1.txt";
        File file = new File(path);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (String name : names.keySet()) {
                bufferedWriter.write(name + ";" + names.get(name));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writerProgramInfo(List<String> infos) {
        String path = "C:\\Users\\Byron Dong\\Desktop\\1.txt";
        File file = new File(path);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (String name : infos) {
                bufferedWriter.write(name);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void searchProgram() {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        List<Program> programs = programRepository.searchProgram("%上海%", time);
        System.out.println("---------------------------");
    }

    @Test
    public void previewSearchProgram() {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        List<Object[]> programs = programRepository.previewSearchProgram("%张韶涵%", time);
        System.out.println("---------------------------");
    }

    @Test
    public void testAddOneScanVolume() {
        int vid = 15;
        LocalDateTime dateTime = LocalDateTime.of(2017, 6, 14, 0, 0, 0);
        ProgramID programID = new ProgramID();
        programID.setVenueID(vid);
        programID.setStartTime(dateTime);
        programRepository.addOneScanVolume(vid, dateTime);
    }
}