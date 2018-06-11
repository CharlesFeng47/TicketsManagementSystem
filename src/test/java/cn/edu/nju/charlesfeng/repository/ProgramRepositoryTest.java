package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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
    public void testAddAll() {
        Map<String, ProgramType> map = new HashMap<>();
        map.put("vocalconcert", ProgramType.VOCALCONCERT);
//        map.put("concert", ProgramType.CONCERT);
//        map.put("ballet", ProgramType.DANCE);
//        map.put("drama", ProgramType.DRAMA);
//        map.put("exhibition", ProgramType.EXHIBITION);
//        map.put("child", ProgramType.PARENTCHILD);
//        map.put("match", ProgramType.SPORT);
//        map.put("quyi", ProgramType.QUYITALK);

        for (String folder : map.keySet()) {
            String path = "F:\\crawler\\" + folder + "\\";
            File dir = new File(path);
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                String filePath = file.getPath();
                if (filePath.endsWith(".txt")) {
                    String programName = filePath.substring(filePath.lastIndexOf("\\") + 1).replace(".txt", "");
                    String name = readName(filePath);
                    Venue venue = venueRepository.findByVenueName(name);
                    Map<String, String> content = readContent(filePath);
                    System.out.println(map.get(folder) + "-" + venue.getVenueID() + "-" +content.get("time") + "-" + programName);
                    ProgramID programID = new ProgramID();
                    programID.setVenueID(venue.getVenueID());
                    programID.setStartTime(LocalDateTime.parse(content.get("time")));
                    Program program = new Program();
                    program.setProgramID(programID);
                    program.setVenue(venue);
                    program.setName(programName);
                    program.setDescription(content.get("desc"));
                    program.setProgramType(map.get(folder));
                    program.setScanVolume(Integer.parseInt(content.get("scan").replace("人浏览", "")));
                    program.setFavoriteVolume(Integer.parseInt(content.get("like").replace("人想看", "")));
                    String poster = getBaseImg(filePath.replace(".txt", ".jpg"));
                    program.setPoster(poster);
                    programRepository.save(program);
                    venue.getPrograms().add(program);
                    venueRepository.save(venue);
                }
            }
        }
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
                        time = LocalDateTime.of(2018, 7, 9, 18, 30, 0).toString();
                    } else {
                        if (time.contains("-")) {
                            time = time.split("-")[0].replace(".", "-");
                            time = time + "T00:00:00";
                        } else {
                            time = time.replace(".", "-");
                            time = time.replace(' ', 'T');
                            time = time + ":00";
                        }
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

    private String getBaseImg(String path) {
        File img = new File(path);
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(img);
            data = new byte[in.available()];
            int result = in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        assert data != null;
        return encoder.encode(data);
    }

    private void createImg(String img) {
        if (img == null) //图像数据为空
            return;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(img);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "F://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


}