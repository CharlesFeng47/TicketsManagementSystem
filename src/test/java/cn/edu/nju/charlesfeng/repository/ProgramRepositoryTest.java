package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.helper.ImageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//        map.put("vocalconcert", ProgramType.VOCALCONCERT);
//        map.put("concert", ProgramType.CONCERT);
//        map.put("ballet", ProgramType.DANCE);
//        map.put("drama", ProgramType.DRAMA);
//        map.put("exhibition", ProgramType.EXHIBITION);
//        map.put("child", ProgramType.PARENTCHILD);
//        map.put("match", ProgramType.SPORT);
        map.put("quyi", ProgramType.QUYITALK);

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
                    System.out.println(map.get(folder) + "-" + venue.getVenueID() + "-" + content.get("time") + "-" + programName);
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
                    programRepository.save(program);
                    venue.getPrograms().add(program);
                    //venueRepository.save(venue);
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
        return "data:image/jpeg;base64," + encoder.encode(data);
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

    @Test
    public void testImgHelper() {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 9, 10, 19, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt_start = localDateTime.atZone(zoneId);
        Date start_date = Date.from(zdt_start.toInstant());
        long time_long = start_date.getTime();

        Date result = new Date(time_long);
        Instant instant = result.toInstant();
        ZoneId zoneId_result = ZoneId.systemDefault();
        LocalDateTime new_date = instant.atZone(zoneId_result).toLocalDateTime();
        System.out.println(new_date.toString());
        String path = "F:\\image\\1;" + String.valueOf(time_long) + ".jpg";
        String poster = "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\n" +
                "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\n" +
                "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wgARCAF1ARUDASIA\n" +
                "AhEBAxEB/8QAGwABAAMBAQEBAAAAAAAAAAAAAAMEBQYCAQf/xAAaAQEAAwEBAQAAAAAAAAAAAAAA\n" +
                "AQIDBAUG/9oADAMBAAIQAxAAAAHrB8p0gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZWhy97qrsuXnh045rAAAAAAAAAAAAAAAZvI\n" +
                "9PR7qYXdYWga44bgAAAAAAAAAAAAAAQ5lTE7adLrcH1NZ1xyWAAAAAAAAIJ5BAApWbJBUABm51vn\n" +
                "+2k/Qc10RqDiuBSu5uheK9fL6XRQ9/ZfRz8fX2U/nCg4L9Ojk5bMuD30Rj633YvH0cllKOttHrYK\n" +
                "SFAAGNk4Nj1cuov8jfwt1o4bgZdiSP6TCKSxYqoXM/o+itS2Sc30kOc0cHP1fE2+aFvHym9a8+sZ\n" +
                "U/dW8e5PdkDOQAAPzWaV7WPz7es5T1A8rUCt9sPRpmesrS9fLkLtzfWk2sLdtSvm2cuUc/Kd3Wcv\n" +
                "E7CHCci/HB52kk8U3Ja2MZAAAAhzMKXtpqX8TxDrBx3AVbVXaNeX7nfS8/59u7FOJ6CzzmlMWMPa\n" +
                "onMb3y952n31l2Oitzyk6K5fnS88t6N2j78fW+OeQAAPz/Zx/frZa+nFlctusHHcBFK9HPT5npvz\n" +
                "r2c+4u/k/ewt43UwWjAn92DGue4vL0u/cDU8/SHV80Oul6R49jJi7VXC0stO589uFQAGV85a7300\n" +
                "Nfg+hOmHBcCr8pWfay0MKC9111fWRr61+Z2jclH6sY8MS7czvE2uZPzVleHn3ztmvX9fK/F9epnz\n" +
                "1/G6HwN5xx2AA4SK5H62XP8ATxdBE6w8rQClJZb1g4Tsa3oVyf0Pi6/fTuav5T+lXbPN9FnWrhyT\n" +
                "T8F6UNmDSLc/zx5+l2GhqdlJPvmPupj7eZp/N9AYyABhU7nK+hnsdLhVaz2I4bgK1nN6q+5LHM71\n" +
                "mu4lnqjN/Tea6Xtph4OjFVozSPnt8zSz4u2lO5N86qyWoL/dT7zv3U83StoHk6hUABlcV12H6OdX\n" +
                "ci1Yaw8/QCjV0fX0mGfma/vGcn3pS3jL0vv3WOc2ddhbP8X4dYj92m1cPUsY1J1sq1D5Wnlo5/Hf\n" +
                "S+5mnnIVAAcXUsU/Xy1/uH3OM3h52gFiLN8e1lofKXraNFQWi+oUbN1z3mHTQ5XvmtPDQ6Dmtleo\n" +
                "7XNb75+Xc0alpwxtkSFAAHCRd5y3o54/QasVJ1Rw3AAAAz61yn0Rs4+xWyn3NWswwdfJ2tY809Gh\n" +
                "RQ3aHq6hrTVqLQzkAADlMTc8epnjdDXnh0w8zQAAADxib1LWJLPOWrNmnQ8nzbr26mfoKTDWi+ax\n" +
                "qwzMZCAAAGBV2K/XWnoT1atgc1gAAAAPNK+sgk9oBACOQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//EAC4QAAICAgAEBAYCAwEBAAAAAAIDAQQABRESExQQ\n" +
                "FSAwIiMkMTRAITMlMjVwgP/aAAgBAQABBQL/AONLtyKcAYsBhyAqaDl/v778ZgNSE75XJpIPtf37\n" +
                "pVwRG56MeaJYyu5b0/v3zFORuaXCdzS4UCFgfvkYwR6umcjraacAhNf7+3Sx4+WbLPK9jMa5Lq9X\n" +
                "9+5Y7YE3rMMfuT7inZ7pP6MuXDPTYf0sCCgPVceNVux2q3IomnWDTdFn0IMpLHvBAA2xARa5iXZE\n" +
                "y6syYugj+0CUGPi6wUsakV5rwLl8bDunFRfFnrvWaoJLy7XlbtU+gmwhk+PCY2E/yLTMc+2LZKkN\n" +
                "KYFf0AuYFoCJsQLDgg5uTHWTaa4hESr4/tHhHUfMI+r9jYx8+05zTF52LVHiG18e2f1O3uZ2t6G8\n" +
                "mwjK6idV5rkxXT26fA6407H2x9gn4hXEJkKyELkB8HMmJ4xXhYHLPY2P91xcCxteEv1H5njxp5/j\n" +
                "MFNI87QYyvYBGw9DRA1NufxXq80YTgl0TExjm9OPxxSrpx7O1rmvLQTAMX1BprGve8e+dnmOFsNa\n" +
                "UizVzmwDmvVR22VvMILHNhKiUsBEwG6NSo3Gpvgsnch0efmZZUvI+TCVzE+0xS3DsXrrV62wFyVM\n" +
                "BzfSiofPI2FRT2i6rfOaPBW3qNPLP998yEqLlq2X0lzOi9eMaoxPXEKwaNfF/Ha7qvx9raWXrvbF\n" +
                "lcFVO27TXzbG96LByCFLhSvtEoTZbV01UsVXUgcerrKsM44HRWwrVV0wpwR3Qxna8mS+MZQYga5J\n" +
                "JfIdfAMWB7G3/wCrtSrNFbDo06rO4j0FHUs5emeyWu+pZrDFRzRXtw4rL+3V5pVjCtJtXJiCjtZV\n" +
                "I3uXO25MF4nPTZWxikX1i8klVguT2NnEt216JsbnZgTNdoy5a3oqxz2s2twazK9pNoMfTW+Xc2HZ\n" +
                "G5rQfQaZW6x7PvK2CUEMxExC21JW1F5PMdTGpnmMFbCtDrMEsxYHrvWCRVG7BUaF2xbgGGZ+ih+J\n" +
                "myRZt3V0b4Hr4vQOO6XSF2uyLIYOylhH3Lm9O3VyvYF8Y5EydWzFkB+la/6duyAhVXCBX69sMRtj\n" +
                "eqwde4yUUVVqrvELCjJZsq4N5MzVMYCHWAzvVZ3RFjeq1ldA11PZ0UGJ9vSmJTNpETzh33hYEln8\n" +
                "u3VQXWrVfirU64yit8E+ra/9eP8AspXz6/WK61nx21Rl0ypurVadi3fGlfqxVJ2rEu2bGdswsWoE\n" +
                "jmw5yWpsOA9eEyVaANAJ4eOt/hVb/ev/AHia4trSCvXfS5+1TXczZ6qu0BopOsHj2y4njaDIdZGN\n" +
                "jWaD9TrTY3CIQGtaXbXj3cdh8vrCHVNsLhK38s9WMFvw/aNb+LT/AKKf8posE/YdbbWuxeY7YPtW\n" +
                "23w6/P6TtQJNOHrhb4hl67VB9t9nNWjt6BFAjWmFVFGTmzw6f+puSUAhvVW0enLBJpP+LLE8+X2d\n" +
                "KowIUHrZNhOxrsYlyGi7X6YrDmegzwOrOEL+BOjj2kMVraEtv5tTkaVbpOgFxymPNBxyhnx1bxvK\n" +
                "xCWypf4oqXCAGyqxYHi9vrtu5WLyFR5Bplx2fo/3MigBt7AZkbiUmGwZy6thE3HvB1uvwO54ONXP\n" +
                "1rHUKoMRNjuIU2TwFhWGw1t0Q6Zj7BUf8kGvtcJpPHTa5B1qXj0r0RJGnDshI8NaRRWptYtFCRiF\n" +
                "V2vda6Sew4A4uXkvsw6oDiu2XHbtMZBMsGnLBfbRTGBZbZ+IwvpWexak43VVnNsa1wkxq+eWeMBr\n" +
                "2SdWBHnuDnWuHgpOR7ecmtxgKxhnC3nRsThV1DAMprzntMztOfG2q1SLNu0S11lKEZkDIYIVz0SR\n" +
                "8p/ruRE76iYJ2uzZDNkFkTZ4/T2M7Gtnl6OaaPGejYzkt5y286drOg6c7QZwatdeM2FRWeYMZkrs\n" +
                "uyqoEMkYtMrFMpeOLLnWxYNDtiru9dw1zvdaYDuLC2bDaaVhtq+JUqx52cDnTtDnG/GdXYZ17+de\n" +
                "/hXr0CVnZZMvKtFBXAq8Kzqmmcu84Qg1csfLuTEFCS5HezxGJTQcm5WQmqpVcU2/Ztm5YJH4ssr+\n" +
                "NR9RWN+UKmQ0GBDFrEbCLUHXH75ZA4JQ8ifYfdSF+Rnz2kEzttbcC2fs2mCtCD4njlyxaFypWN+K\n" +
                "BEQHI+XbD5zqv8LxP8R7Fiqmdh0Fd4pCk2ddWVXd7JCJxPLDlNhseDm8udvD8GOUcuARV1QMJn5d\n" +
                "vJH5vsXLI0y2LX1qdNjna+qxdj2zqqYUsjri8sOxwwQNqq6pSjxUwK8NMrA+1NQDtX6RXV1K0VKy\n" +
                "KAou+3wji6qD2SlZAIwA+jkHm/8AZP/EACURAAIBAwMEAgMAAAAAAAAAAAABEQIQEiAhQTAxQFFg\n" +
                "YQMTcf/aAAgBAwEBPwH4tO5lv4LF4Lf0L+eC2LTm5MzK6Wtsm+TMndOCZ6Ub3/Wrb37GTFVrbJu9\n" +
                "laXdUyh0tWVUC0+zmL/k7CGr0twSVUerUuNM7id8k+4160JR2N3atc2WjkpV8UbIcsai0C+jfuiq\n" +
                "qbLQ2J3qGqUKORiUkCcMy9Wpp50uRLe+TMiR7mRk7RIqY1ci73wZgzFmLMWYCpXQjcS58Fi1yT0X\n" +
                "Jv8AC//EADURAAIBAgQDBgIJBQAAAAAAAAECAwARBBASISAxQQUTIjJRYXGhFCNAQlJggbHwMIKR\n" +
                "0eH/2gAIAQIBAT8B/K3dKY1PU02HZIyzi32GAeDyaqxC+G+jT9hhjGkMJNNToNN9eo/0rZ6Ta/BD\n" +
                "FrAGnn1rEIoXwrt6+ubjemUIo60uAjaMHrR7OTTa+9HCsuzZLHp3b+fGpZbppyVAD4qdtRvwR4ey\n" +
                "ix2PP5U8ACfDP6PF+GjhYT92gLCwyljEi6TXc9zu9XMhom53pF60zenCjbLY/wA2ppPqiDz/AJ/v\n" +
                "MdpSdRSNqF62z2Yb0cLH02rEYVlOq1/hTX5DijgN0J3Bp4l8RXa2cCa5AuVs8RjDDJpttUOKjl5c\n" +
                "8p8Kso22NSxsvn58Krfu/YUQHiMnW+fZyXkLelGgc8VFCz3d7U8NhqU3FYbHFfDJyrnWIhEq+9MA\n" +
                "DtwCBe7U/rU8AUnb981wrwtqiP8Amomc+cWzNPiO9BSbaiscKa1ubjLs6UkGM5YrwswPArARDf7t\n" +
                "YqfzKM0xMqcmqKbENYtyoSLfTQN8mkAYswFvmakZLBZOta4nbu5Et8KweGMVy1cqmbW2v14Io1Zb\n" +
                "26VIl0O1tOeGRSS78hUU2JlbwcqxHeFB3e3yqPyg1NJ3aF6eZ3fWedSRPPGjLS4TUQ0vPLGYkW0L\n" +
                "TG/BA0arz9P3qaUGJrH73yz+jx2IA50MEF8jEUcOxTTrNRIUWxN6bBoxuxJpMPEnIZPIqeY1NjTI\n" +
                "CE2oNemXSeAD6r+2phpS4HmzXtGM8xQxsB619Kh/FQxER5Gjioh1o9opeyip8XN02ok27yibNem8\n" +
                "J2pnLc+BJ1aMqdtrVJOroY/TlxLfu6jbS1Na+1SW0+//AAUp2sKfdfcUX1LbhjZFG3p786nZHBPs\n" +
                "OKNwOdND1FLFbdqke+3SlOk3oKdXtxRvGGW/60zRsW+XGGKm4q5/If8A/8QATRAAAgECAgYDDAcG\n" +
                "AggHAAAAAQIDABESIQQTIjFBUTJhcRAgIzAzQlKBkbHB0RRicoKh4fAkQ1BTY5Jz8QUVg5Oio8Li\n" +
                "NDVAcICy0v/aAAgBAQAGPwL/AOGkbMLqzWNB0N1NYghbmBQkja6n+ARfboaRou1cXeP0uvtryL4+\n" +
                "R3U7N5z3H8AxaSoZBwIrDBo9k4YmvWKfQo26xQeLofwCOaSLWRre45dddFh92uix+7TypDqkc5Dn\n" +
                "1/wBUbz6uYQOw2ousAJHM3pXXokXH8AgSIbeOvLf8w1Yy/8AMNaqa2ycrHh/AARGZJCbIopV0vRt\n" +
                "WrGwcbq1eixCS3HnWIoUYGzKeH/otWZFx8r98FUYpXyVaAZsTcT38Mzg4M1NuF/8q1Oj3OLebUV0\n" +
                "pSkrZ3tfKpJlUhCbC/HvJI3Nyh38+5d2tfdlegZYL5eYaKrBMzDeMO6sDK0T8FkFiaKxxPJh34bZ\n" +
                "e2sDBkk9FquaxKQRzHeajR85eJ4JToyyPKW2PrddNJchG6CXuAO8ABAY8+A4mpZ2bGxNlbq8Qyz7\n" +
                "Y3FRSuiK8l7Wx3w0JHVZ1xYcrHOtVGwxAdHl3mzuMe18KNjY861OlSnay2QufcnlUAu02EX9S1g0\n" +
                "2NGi/mKMh28qzOPRib6zivb1ddYV0eaTk4GG3WCavp0brGvIXDdtqd47LiO4ZgnkPiaGO2Ljbuaj\n" +
                "RulxflWo0YYn89zw7a1IJZ2zlfq5VYd1iJDHGCVGHeawuxfZxZ8fE6VbdrV9xoxu+UcqgWHbUaTk\n" +
                "y4ZTgGQ3c6nElmeQYgVNwB3mP6bFe1uh+dZaRCfuUZNZo5NrZg10dHPYxrSIpNh9Yd2djvq2qhB9\n" +
                "Iubey1CO9+Jyt3RIB4Jtkf0z8u5q4sk58x8qwxXSPjJxfs5VkuQ3Acau3lGzbuiKPyjf8I50sMal\n" +
                "337/AMTRlksDawUcB4nS/wDFT3GnZJL3mW4K7jUW3jxmQnK3Cof8E/8A2PebWikdujn5VmkC9q4a\n" +
                "8GV+5J8q2ZZ1/wBoT76n0Z5CWYhgzccu9ZZOgRY0YpXDFDhsPP6z8qxzG988Pz7gkc+DQ2XrPE1c\n" +
                "bu4ABiduitenPJ+J+VEk4nbpNz8VNK1sMkq29hrWHc+ki3qqbwTa7XOsPYd5rUOt5Fj2JOY7za0J\n" +
                "/U4NbWi6QPu3q0qgHk8VbMix9jFKk1WKRctq+LhzrwWtA+vu/GgNIELLzBz7he1+Q5mhLpnhZDuW\n" +
                "1xfkoqRX0RljeMHDhv67CidFlMTf02+FFVKTDmNlq3mBxsBHXorzpt4jAth5URjBb0Ac6M02crZW\n" +
                "HuFGSTyjfh1eLwyIGHIild4BKMVgDwoSNFIGPoxlvxosInDKOk8du+IhXWxg9JmKe4/Csopl6sWt\n" +
                "X8c6mSSHAGe+yN1eVP8AaaCBmufq9zRb7tZ/0mtIcdKOEYOq5NzUTRmVgV28rm9q8x2HqYfGvBTY\n" +
                "h6Muf41g0yHB9vNfbR+hzlAfNOY/KhHPDqDwPmn11IzZ4Ml6sqtro7/a8WkSSsqEDdQuDpBxWOKQ\n" +
                "5HsqInWIW4I72FSRy60w52Lg+/vXK9LcvbSRruUW7hlOlpGZCWs4Iq50nXfYNWijVezuYb4TvVuR\n" +
                "oGZdXMownF0JByvUJ0TRpNbjvtnK1jlegulwmJ+Bce5qvBPjX0ZM/wAaw6QhiJ9Lon11i0Z9X1b1\n" +
                "PqrVaXGFvlc5o1MdBfDf92271VqMGEqLNG++rxbUf8vl2UGU5HxMfYvvoaO82CS9xZb0oZvBr5xi\n" +
                "Na4SBhuFhbvYI+vGfV+du5KBvcYB2nKljtorootbMV+0f6Kt9aGze7OsWgaXiA3xSm4+YoxOpinX\n" +
                "fG3w51jwM+YFl662y8Z4h4zlUQhfFhVifwqxFxWLRZDEfR3qfVWr0yPBfLHvQ1i0V8H1N6H5VqZ0\n" +
                "wOfNbc3ZV4NuP+UeHZQdTZ13OOkpoxaVZXAuG4MKYsuEMxZR1eJjCAk2A3ddQ6sE2w3y3Z1KqC5/\n" +
                "OmjIbFjJ3dQ72aT0bRj3n39zRwRi2sZH6/WVYonv1cR3MeaSjdImRpY9LIjlHkdKXdf4VIpdEnXe\n" +
                "MXnDOk0ozRq5HRZ7Z9nOnk16WWMKM9+deXj/ALqDKQQeIqxFxWLRc04wk5erlRFr+kjb1rwhLwen\n" +
                "xXtrXwW1nHk9WIt70NNDgXWx5tfzx1UHXcfENJEVxLwr6RYBsBbAWp5DEMG5bGrYBh4nFuPeq3Fi\n" +
                "W/HuSSRxM6LsAisUcMqtz3UfpjDqHHuHX4NXxx7q8GIz9iO/urY0aY/7K3vorHo7XHpsBSSkQDDe\n" +
                "wzb11dX1ib7W+Fcm5dzXQnBMOPPqNG4wyLk6HhQT9y52fqnlX0kdHdKOrnQ0qLykX4rWINix7V/E\n" +
                "Q2UZhSevOptFOjCNlBPbbsqQNpI0eMWAwx350FXSWeWXzSfX3mENteicjRXVmSK9xh3rQDY0J9NC\n" +
                "Kx/SGgld2bb6DZ/rdXhIMY9KI3/A1msw7Ym+VeC0eVu0YR+NatsLybxEvRXrY1gGZ3k8zUknoqTU\n" +
                "OPQwywLmHbNsqPRBxHJdwzq2sFxyzpWgNwTnb13+Hs7o0qHyibx6S8qyOxIN9eE35q/buNGJ88F4\n" +
                "z11hcuSjFOmeFSxei2XYe/0fsX31pPY/urST6DIffQ0m2wkSoOs27zRtTnvu3AUTFpDu6rez5g04\n" +
                "1UOAb8V7Gl0eVgjIMLK1bEuAn+SSPdWzpcvrCn4V4TSpSOS2X3VhjUKO4kUa4mdt1+AzrEOwg8DW\n" +
                "y1hysDWquWY7UV2te29bUJYwcxvY3PeTR8ElYDs31pI/q/AVpX+J/wBIqeNzJtTkDDJb40cAtff3\n" +
                "8LJC+FbXNst9aRLqnVSGtiFq0iOWB7SWGYtzo6O2aqbo3V3l48UR/pm1ZTq/20+VW1EPqkI+FNMy\n" +
                "BVkN9k3saE8y2jXMA8T3CzEBRxNF4r4QbdxvDavVrhyTFvzPwp7TaYztmwWPD7xWrEek35vLa3sN\n" +
                "amI5DaMxzN+qrsdWHO0MPRf899b9KHWUFqxYldPTXh2ir00py1rl/VRlP7xi/q4fhRl/mMX9XD8K\n" +
                "ckWaR2cdYv4hY2THFJ0bDMGh9HxtEB4RCKj0eG8fFga29Xh6u+ta32sr0Y2dc+BT8zWU4jTmmIr7\n" +
                "8qkAtIqm2s34TXhZWbqpFPSbaNFjuGda2U4cXhGvwvUsw2cfFvNUUbXEW883p2cXEWZHNv17qMzE\n" +
                "lj5S3v8AVWdsYya1a6PeOkPSFDRI74HGLHyT9ZUuhx5Ajat5qUNFTe/St5q1gj6cng0FaOF8xgB4\n" +
                "hMAZ4JOl9U1p0kbAFb7x119LnuCL3wMVv7KkleRzHuALXz707WFVzZuVXjtCp6rse2vKhx6Lrkax\n" +
                "JjiXMNbeepf1lWGUAC1lRdyfn10VkGzEdruFF6Umz86IbRQoUdN5NZ7BQXDhjHRT50MyM75Vb+uC\n" +
                "fZ3FVFvE34D8qZNGzUdKU9FfnSKBjnZMh6z+FYR4XSZM+0/KmeRruc3ejpMkihF2YlJ/GhIQRGvR\n" +
                "B4nn4iCEHakcewV/pHsPvppMT3vuxG3S5Usl2vmOll7O9iXgfCnr5UWY2ArVqSF48P8AKkdAXbcx\n" +
                "ysRysKCoi5ZXLfO1aQHwXNn2fZ8O4/hXjEWyrKt8+NPIFtv9Vz/n7e7q3kUawYd/HhWoEQ1vEu1h\n" +
                "WPT9IxD0Bsr+dajR01cIG05ysKYaMl23YjuQfrhTSO928+RqxKp+jKb4eMlB0w2O4jxI0tfR3dda\n" +
                "X4NRrcgC3XX0UKGc9fXSxydLvNl4JR6xULmMk4dUVXgaKywzoOuM0SZDc5m7sKjWE4s7taQ7quWG\n" +
                "eflT86RtDs0x2cGO+IVZm0fRb+c0lzWaYnBtsBjirDBoU1uGzhFdGGIdZxGv2vTm7L4BVtF0Qv14\n" +
                "fiatIkRTzULHL71WTRzM+6yvkPXVp7LH/Kj3es8aCed5saUW0rzTlCNw+df0G/4D8q1g8ix2x6J5\n" +
                "+JjhE0oje1wHNTpPPJqlDdJzlnUy6RpCOqjwbYs2ovPMWlIyW+4d5sGG/wBRre6mw43RukC1z2ir\n" +
                "pIJY+eG59leSS3N0w/GjqlR8XTYnCCOQyr/y7Rf7v+2rf6u0X+7/ALaumh6Ip53v8K6UC/dJra0o\n" +
                "j7CAe+9XmnkI+vJav2eLGecSX/GtmJYhzc3PsHzrw8jS9RyX2UEJAPBEGfsrFb6PDcXJ6f5UQFvi\n" +
                "6ROZNC+8bDfA0VYXBr6PLmp6DHiOVPo/mqMS9Q5eIhDbsqnJBw7QyF+NRsobcMipHGsAjmHWYyB3\n" +
                "n7qT2GsoVX7OVYhrA3PWGr6+W/1rN7xWWle1BXl4v91+deWh/wB0f/1X/iE9UX51npcn3VUfCtuS\n" +
                "Zu2Q/CsQiS/MitqdOwZ1+z6M5HpSbIrw2kYR6EWX41LGFF73B4kGjfySZdpoBukuyfVWLgdlvnQJ\n" +
                "38e2sMihhSyw3bzWVjw8RFiZcItivUu0LNiC+2ptV5g39n50+sdmIe20e8zhT1C1eDmnT7MlbOmv\n" +
                "95Aa8tC3albtGP8AdXk9H/uNeS0f+40xCaPk2Hjmazsn2Y708raa7W4IMOdDW4pW5uxNazR1CsPN\n" +
                "G5qTUDFFLmo5dXcWWPf0T2GhHGbYfNO+iOEgv6xRB3GmjY5/r8vFcKmmEsW3e2e65rBGe0njTvEw\n" +
                "wyZsnXz8ViiF+daOh81MZ7T+j3Db98pX73Ckf0hfuT/05BIP17axC/LOmQ7iLUNaoLrke2kcSFwr\n" +
                "XCtv9vZ3JJHQMvCkXPIcfE6gaIpkZgCzWrDgjBxdHhuqUBIydrI7t9MPo6xso3jxRxX2tnKtHf04\n" +
                "revuWBswIINBC1/V3NI+syxj9eusKgAch3COEov6xTSHoLsL8aMR3xnD8u4Y/Q93DxOvJn1gIbZj\n" +
                "JFfSr6TrP8A/KmnU6Tja/wC4PyptXr8x58ZA8VZgCOum0cMA19ZF1Hl+udcnHSXl3cCZytuHLroQ\n" +
                "hvBxb2tfE1Acu4SmTrtLSYOhbKlPCQW9Y/R7iuOw+J25JyX6KraklSVwzMAQbcq17SSM2eS2rWRa\n" +
                "Q723g+LLMu0eN6McoYum6WPf7K2dJgf7WRrb0qJeqMXNH6P4NW/eN0moIeHePFIwUIdm/I0BFG2R\n" +
                "BDtkPF6+TbYdEcFoJrcIBv0aWIG9uNSaQrmz+Z4y9s6V282sOEW6qCqLAd7iwjFz/wDeX//EACwQ\n" +
                "AQABAwIFAwUBAQADAAAAAAERACExQVFhcYGRoRCx8CAwwdHh8VBAcID/2gAIAQEAAT8h/wDjTCpT\n" +
                "yWzR4cRKtcPExw3oPGOn/A+RwoPyDobcPiaK45Mu5/yonsNxN/8AgL3RaQrwo0MbYsP1V+drm85p\n" +
                "LxVgiI4R/wACQWKIGbEXck61BdFGpvpw1cLLHGGjj/wInMgnW01L6cYdigQUkm91OjIuR/wErKkX\n" +
                "iLZn0goBRyNASWpOT/gBh4UXWttsSHQatkFChekKld4aH/hBgVrE/VDmBxTu8KgVBgiX62DbsJwM\n" +
                "0UabfRsUKgw5my1XQ1ohiZ+bfQMk8jaCSW6+lyDZek8ipbFCxydGPdqBYZAe5pDKvdAb1N8ZYBbS\n" +
                "i9W5VMcKcNHpSiIAXVoIt4SR+iy7W8G7x4U+Np1GErR1ziguTZe4E7/RJvecNY4FT7TdI04Oc9vs\n" +
                "Q/TlzZvHLFFIDELjRxUYOiLWE1CRlJIRy+iRbBON4X8migkC2yh7abkQxzO1AADBRWAaCVCeFIVr\n" +
                "VdBnmntSsSMckb36/wCm9QtWSpNxPFR9zgeJmY5YqSNiD4AatGgkeBPpM+7lnh8/j0PLC3W/CjYk\n" +
                "ZOfxTtzoAAgLAeqhgMCQYVXjQsXQnGYsAxoWY3fswWRcka/60Qti5Ml3iowVxJWF1/KSzmxAxPbt\n" +
                "9EhL9uoR+U/uogTMRkCv5rWcufxRHFwlhAfI1w3Clzf0oxBcoQSssGhw9EkhxW5SzUcGyfMUoFWA\n" +
                "pCSQlbHEnTj103pJ5OlH4j4U2tWspaHNaRqKz8duRj1hpS1xuKGA0oy4pzqc0y2epv8AaRyUNptp\n" +
                "i83zVpu102JEup9IRrAGfiP3BXvzD3V8mTqqDtKt0SLRex9ML0qTaitAHEjW2xRBBNmkXfdywekc\n" +
                "JsZP4Qx3oEgq4mvpcCUbj+q68+Wwq5kp3H6+0fomW9q1k2Q6gSnlSALmDoZZpqUaBeKRjM/RDh/C\n" +
                "zUTwx9jXF+In2pGHtP4tO5ngBSso+BD30LXDHA8Q+juTIBy1g70/YiLA+DOaVOLjjCmRv4p/V4QR\n" +
                "zdNpBGN6YoAtCzLN7tbTbgULZDB6tDmGeLwqQBbAFu0Vwaq9G1UMaV4wdny/2yAuzdXqNVEJdDfH\n" +
                "CpsoFqidhUEFAyY6C/UGWQIadhuc6MedS/ReNPOd32wkIbUhOZ2/lQ4rQCr9vS4NX3s03KPcqFxg\n" +
                "KQ02DkEwGau/APRXw1NjfvNWRTqBeWnrFCpfny7OaP6bZOR+1AfYtkIXrVjfLRH7bx1YiZaYrUBy\n" +
                "DZbfar0bxpPBtQ7Z3mG0L6dBp1FjylYumelKIrAZWrLvb2WctqsuJoQeFqLR4LvX0NpxQmlcaYEO\n" +
                "AFbmDfjco5LuhakHVXBBIuh/KBTnFn0N+81Nx0m72PeKhkLeP8TpFQf9LA05NRzE11m60fFRpZwW\n" +
                "c9+dOY6yb838VMGxfZ+W3UjoCCd/etEEwmXW9CcXpMDeZc4+nacfLx8vQT9XFe9B8lMmQI400bKd\n" +
                "Q9lHXXwi4XrCiE7TdaONXfxFro/NQWMMAVbNqObbcbYHu05MmRJKksnPwnSsN6R/h60Ixw3l4H4V\n" +
                "ZZsZOcw+9a81Uvzfw+KULaWuyf01KyAONqceFBlhi3Fe/GZ+zaOxS3UAPjIMmaVZCIGsB/FQTlmY\n" +
                "Dc+nYL4BydvSFSItixY8s0je3e4PQrvbQ/s4NTr5yCFsdzqYatmYgRKCOChQUwMGENZYSlECT6MJ\n" +
                "Sx4oTT5goMLSJI0iElkTNSg1vJOrxSLtMAhOO3Op0aKX+44996uaI2TY2fw0nddrYv5KeqDIuR1e\n" +
                "KVyS+wgk4rvJN64+KZahUygQX1u50qyWJCK7EfSeMd1T6MiPlGfK0UAsCiykjShzT0RfobumhWh/\n" +
                "lhXsYX4qyYxpMpNptI1YJMtAWMrXt5ahoJlaMs8XZ6VZ5ASzm246noA0L0nyFB8o3B+qPC586zp2\n" +
                "2oQZUeA6fblRhka+4PvU/c83aV2NPsXMxAM55o3NxHLQ2W61HkAazRGMUEAp5C5DDz+iyrtXtN6V\n" +
                "NVSy9dIck0tSkED3iPNPbCgZEmbYjIaEOTeXYe01HmdqbBX3/HHxTGp4ue3quVp21oAsjLy2WhV2\n" +
                "neCpinEyXkG03ijMgO6ykjhCJTNvkMHOKFGn7MpB4X9eo4WPfv0rKkkgyceZUEAbVoijVipOgsdy\n" +
                "Kk5tEZdGBoZSJ5M2B/f1/Lb/AE8UsVsPT+aaxFLgh7fRAENsli1571YAyQk21O9TIghQcDWoddpt\n" +
                "bxRIB3xkVoebfuVYvMQdRPmta0wavogcKzhN1foHWr1S+CSyPGpHxLE4E6cMVwp1BaFASXIPalPD\n" +
                "aQBtfHL6LYx3tZ+VckLO/wC61gGI967BIwDdNANZMs0qvf63pAOyNzirlra4kgombQZGXvpUjim4\n" +
                "Wjxn6Jpk5bvph7UYVbXu6Pao8c+QVtMYlKZJgpTrYTldPQ26SpAU0yTNIv8AH0CC4Zla0MyICIy0\n" +
                "GB28VlVEsMN0/wBqmxEMIMXZbconjSa+6TqIGgbOtboOAvFdhEjqLnOlETAZaOlrnoseCrrBmd38\n" +
                "KurTOa3gKcZQaZtH2HdZ7lDjRdYMURJxLyo0CQgpL6pNojvVnAR1sz9SKe+BL0LX8UVcxiRKMmNj\n" +
                "ap/j08v5bUsIxqzY/jMb3pK0dB2xSlYsfF/kUjsHJ4VEQ5q41I6EHStAYiwGsvFlY40oY1hn4/r8\n" +
                "WpgkAaBMdCI/3Qs54wZBxd+9NqTBhO5wcnOkYg5RqT8/FQ6iLd6c80BGwHtHXHetJQu6nvg/lS0R\n" +
                "ADd/RWX7k7D4+xp2qyb06f7R1jWWatYvntttMi9FPZNhqtO35+mKNk4NoHFqLHqj1U1JhqkzmUhY\n" +
                "S3KmU23W2iiKRerW2/ErJo21TB6T3rxAW2cOA1GdOV5YUIVvjXtQDHFffc1DLUShpzoUkt0dnV1n\n" +
                "0BqgiYt8D0YoJgDGsaxuqE+HHLCugoTMGWjvOwoHtzrH8CpQugZ4w3a03VsPCaWxz+wMQNW3JfaP\n" +
                "RrrcQ4a5KPflglq6PpibwPOMdhFa9BNX1hDRTwdR5fNFQl48BIwbEUmI7F11oVpPekXNi7NdfQGC\n" +
                "Inm2TbgB1asdCaJeJA7NlGnqKoXMbgXUbTPeraPFl5pq1EZLxHlmgrgJocE06/0UASIlvgd3LzXq\n" +
                "T8Epu/o4VE6JIWxv02NaiOgsPs2oGFnG32WjsosDi6bUuurYjFrrRygVUGYl+gmt/or2kow/y4kG\n" +
                "3ObUvO5/MKuh6RhaVqL1YHM3tV1ykDIE2OipO9s7kjLaImaYgIvCOVgqNAON5i60ARDBHmoxONfg\n" +
                "tQMsNpboXoMRNoxH3/NDmrAE9FuUNR9O6R2NqDMRnEnuUg4MLC/bQqMkCmaVl+NawGFg+OXir7U3\n" +
                "gHDfvWST7EsCAovte1NAhDZAXmp41ACXvRvP3pPYn6J4Ram/KpeZVC2R14anZLIi0SXBkM0W0xys\n" +
                "HG6pwkfai1bd19nUToon4h9qKcyld6z2g/Dk9CCHOiCexBTFjml7PPoKOEL+P3gZ6zWADJzoFQOI\n" +
                "hEoWJ2pMKD3ZamPKzOp8jmtBgFCOtLKQnzBxPNNk5JLlPw+wXgsmPWlFDYfR0KHDA5Zlo3oVID86\n" +
                "J9BFJDSyytwfjTcwrAV8tGM8Ywu5UGR572iosNzfp8SZP4aqvkgc1XjKA7QpKDGkXu1aZBqz7FQb\n" +
                "qw3U3azdssfyoK4WXqXOSgczMTpr0xzmmYl3n2T1z1obKwTGg46H3acBGJ2FmtVQw1M03KbnIvG/\n" +
                "2GhMZIgzmrdNoNnRFTDYaBpC3ei1qsEEH9+hiZW8jxRjDskeZo0kq9LzuezUX6SoNbl+uptD5bU8\n" +
                "4R8+EVFtTeD4V8U33DBo7G001I8vMHlNWx3cIHbnxqJmiSN+vBvwh9L2g3zJrcxinzIIxHmN65C/\n" +
                "tPiO1GXIQm9NcTvqkE9Tun7UpcrminIliW4mIqNC93XW7Uam3WNj7Q0cJbZ05lq18n42t59DRWiv\n" +
                "ISvfxQm4H0I2XC4xPxvUORdQLiVkRSodZV9QWUdN6LIMCdcx1M0IBGRraIHeCNZxrprmr8qElX+z\n" +
                "qMtYmI33rR0N/j/FFkTTDtKhDc7fagEoINyNBoHboY9+3pEkwxMIzQsGKyQyz6Mbpx1ifd2oQN4C\n" +
                "A9OTI83cjtRDUy8Xd+OjSWF30/wlZIaunKg4/COn2XgUCtqIwcKU1JOjiKZk4TDel3xjnMt0+1wT\n" +
                "0JKzHudV+NKORMVcr9cfVYmf2RwpDkiQSc9perwoRVQRL6I43YZk/k0WaXprnX+8ePD0WRMPFP8A\n" +
                "fd+ydygGJ4qd3wKJTGOFHJXlEkHS1DhKxRbmR9tumwRDuceNYxMNjA3F9TSKwuCHv3/FMLbxH7+1\n" +
                "X3Hu0v8Aj5amWG7GAnH0ZZMSJvH5OlSQwpiR43e325Ztm3gN6KWO2V4T80ke8q1WoNR0WFZ+5paV\n" +
                "pi9SyzogvefxpTBk9LPao+igPpULhEEbx/7l/9oADAMBAAIAAwAAABAMMMMMMMMMMMMMMMMMMMMM\n" +
                "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMPA4MMMMMMMMM\n" +
                "MMMMMMPaEMMMMMMMMMMMMMMMNmYMMMMMMMMMAMMAMMPX8MMtIa5stjkMUAMMPtIMPzv3xyVAocEM\n" +
                "MMNesMPDrLcVlVrsMMMMMPcMNDwHASK3v4QMMMNP0MNfz9iDPyUkCAMMMrwMNEyvz5JwMc2IMMO/\n" +
                "QMPNpraTdUoWGEMMM84MNQdh22L2/f8AADDDvLDD8OlFiEbSr7iDDDS9DDSsX4pY4ObZgDDDJZDD\n" +
                "DDDOWo6lAfDDDDIEDDDDDVOaZQQADDDDcPDDDDDDQCDDjDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\n" +
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD/8QAJREBAAICAgIBAwUAAAAAAAAAAQARITEQIEFhUUBg\n" +
                "sTCBkaHw/9oACAEDAQE/EPtawiAaH0Lzuo87v6HJWUeaw/VvpU3cauX9uSDbEovfqAdcLepkvhfi\n" +
                "BXS1cQa8+2VeY354RWT4JqEWB1TL/vmGB6CU1LpVRvzCIqHmhOIV221BYvl2PWzzvnH21EOuq/2l\n" +
                "0PDlaQi5gGnkNAhbDuA54WvUNdL2l9coQgyKrm1eUFafHFXDMOiP8ppXlTZE1bgySlngSUOfxA7i\n" +
                "gWXCAE3BRXRBmQ98pwHmCZlTDtlyoEKIDDGonBrtA6FOowMeOcgsz2EDd1EKwqDDEU88C0h5MSoN\n" +
                "9H8o7ouuXxPD656YSH5eP6nioZIFdFgT5ij2O4lkIbjDcCnqFc/MKJ2S4R6QIly8dhRqAK7pcr7D\n" +
                "/8QAKhEBAAEDAgQGAgMBAAAAAAAAAREAITFBURBhcYEgkaGx0fDB4UBg8TD/2gAIAQIBAT8Q/q0T\n" +
                "sInK2OnXz3qWIEjnPx/Bs1Qedjsa9akmUMSyeT+P4IJgdJ59aBLeb6d3/koycUC9ceBxZmV1r+RG\n" +
                "b5pcRCIn/h7bcSglpB+fWpkCWt47Y75oUKIHelxLm0npU1YfTz+xSQxUCK+htz5NjX3UMl+2NJi2\n" +
                "ud+E0WLx9t96ws7wAVcglMmg606FZC5kzGxi62OK+R5VnRRBgOC/Xrq+n639u+BWS2Y3/fsYIKZG\n" +
                "mBE7G/N5H3WBuXLl3/Xr7eEV7ER1Yt7Q+VT3KDtzv0sOztx0J6/NAJ15R71EpmiNKbUkASU/Hsx5\n" +
                "ffWlrg+r6+/WpbEqExtkOniNI3EHzIHnTYjcs7Tpvx5/Ppl9OEeMBhgdaszjY/b8HDuJ361JBga7\n" +
                "/PXz8IBKkj0xmZrqvsYbzjiSGj3pIWpHJxWzFrbWpm55NNpMlNDzu1Ou9CCTFYQIxP3WmJh4LITN\n" +
                "zaQsRE360y2bAK0GWxYu558X0rc1dyh2PZDM1M8EgpTOpjJBtZEmaNoDGkd9bZ4aKFz44BULLbbP\n" +
                "Xrp4ChFlrulBei2i+iE+p6u3GzNHO/vSATyDHnU2c5w/EVK4aXVLSnLvrUMy6A0k/A2/WJEjmJsh\n" +
                "503mYOhr3pQS4KJRqffwKQJh0VYuaJy1vQoBgbZZ0fniYkjMbuhV1oHKweXpmhjiWZ9V2w7eetJW\n" +
                "ZaFzSpw2Y5dKDOUIeu996OBIAtrGrv8Ac8Cu8NlPb781K2DHgjYLiZtfLyIo0lVRfZ7cQ4oZRQUB\n" +
                "cmiBBGZm7+qnV5ml3IC2Kwn7+/CGsLg1atZvV+Nu5UVW7qbm/U/e9WjT8PgmkF4Y3n3pbNRLpEdt\n" +
                "cuJ45aPJrZPZ+KH0VZhqIiU2tf8AVXIPWx+aspAds99qbDKtp23e+nVq1cN/PPk+1Bls1KiXM1+f\n" +
                "BiDEXz6UZCAEo2z5+LSEZvvZ/E1AP36N6gembU4Z+rnr+aaSQlz8+l+1Wg8zv8a82mHZ/n3r4ZSW\n" +
                "WLldHpSmGbFkxnl4mUbH53+9KTkN9Y7PzDVg1u8fvoT2pnFkY+e+aUTpQlOWrzuPs0KY8IzgZJbo\n" +
                "Ef61GNrthk2xa2PHNCGliFtxVc/0D//EACsQAQACAgIBAwUAAgMAAwAAAAERIQAxQVFhcYGRECAw\n" +
                "obFQ8MHR4UBwgP/aAAgBAQABPxD/APGiZ541ZMDnWsF3svkf/fGIhblBHkhinUz1OsNEk/vCbE5H\n" +
                "/AfvP7wR0tSlDTqHJ8khjWkSqOjKd4h0T3qgIOpk9v8AAFRikxLAXnftM1mmIB50HDwMYEt7UGPk\n" +
                "H7OBrCBo0rgnX+AkqdBdA0YwWNpbzHwxgE91D8oZE4NwQAKFEutxPP8AgN8QeEIkId0Pxj5D3+7A\n" +
                "frFIeKYQkAafrDUJg5RJ/gEaJU0Wy4B3/wA5/o/6sPA0IBOkjGGCGrrhk4V9o/wEpwxlRLoUILxW\n" +
                "F8Mt2VidTJfHUxKOPhuYQeb9MdibLSIPIMIjrnx/8IaTCFOdQb+4B+4fqdB2uREgBA5EDR94/jtA\n" +
                "hR9x6LjDLFyB0G1e49L1OEwEOEFEiMyPOBGJhwIY9SwfYibeYQMwoQhVMfQBILEyGg2PjCWrFipY\n" +
                "vBPGEf8AgUQ6lAuGL4ybCBAUjlQei6xhd8c2JEEwRgmJJicakHufEKPlIYeFlCADlcOQMgA8J9jA\n" +
                "mEin906fLzPGchFHYks+CIgvBxEIuuRrbUQRcWR9dZxhkOlJR2loc5qFhuk9ESHm3N/fKxoONSlZ\n" +
                "K6KmsAcCZk3INO4kwxhpssuTVHHZkVZ0tRyCiT5PsJMAhoxIqZLLiE6x5zIAMoph3GFrfBLNgQZ8\n" +
                "KO6wyoCA8Ze6CMEWFwpQ894+JxvPhVYeCg5cXBmlmskNy1sVpiKZtffSDJ2EdWOI5DhRGmBwuktr\n" +
                "QQ9XJgjBEOeQCIGlxNywxnGFxNxP0iuZDdMhV41PdEsuCyFmy3Yt9DfoXkWD06z0ii5AaBbvDgCA\n" +
                "IAOPqXgIW6AECCAE1M3GAXFF5rABOAIZHX4bclg7Jpn/AEzkwxVVfK3A3JgTYOIyBQQDMFpfTGEF\n" +
                "FAMahkgfZDEqFYUjryMTL12L9ZEwFhACIW5U+hjdF6V/eWzIdsDmphBgO42WB5Akj0j1x4CGBFQN\n" +
                "SMHAje/oCAKIR5yRC5IUkAdJRWoNJBlgSqwB3kBLb4ht7F6HWBOC8K25oinTQ9UDeMhFB7LAdoLe\n" +
                "WXIbyTqZQ8EDwefrbCQROg+M4OWu0GS8KEKqlEr3WiqCsqxM0soJTE1BB6/ikQh3swHhpQpomocA\n" +
                "b/yKN1whv7UEKPbf8jP3gaV+1MEPJnX6yHPgAAj2WE79IVlJAIQVcd7+w0rk4JEb49cFZNg6wVLJ\n" +
                "QbW4NQBGgHBIUzgVFS39GbgFY40nDMtFusA+olIHSP005tmO5ehteDyhnGfHsH6Pwdre0EMQ9AcD\n" +
                "QcHmX8Vm0blAycbwrQMhFqniRB5jCIBkpAKVzkG0uceseAliSdhwYlC/P1P95V4YU+7Rf2/zGTZS\n" +
                "tL0nhpa/6XDnlcNYuNpiY3hOrpgB4Nz0MW42pbuD2Fev0mTE2wB+VB74QIaE9q5L6FQqhpfMGljk\n" +
                "gsghKRneCELLYXrA9IMrVCUF2iZsTEJfHGQ84PxakhPcMtDvIBC9aVoO6M+VMb8GEKgTmVyjqV6k\n" +
                "+vvbyytFKxDJp2zxnLyvoPxQgcxEgkw8wvzgIIRwlASopWGSrdKIQBBf+GcGQxhqVFFJLPJ90UPR\n" +
                "eG4DFkRCTa45hGwP0gB7fR1kF4OgQqMBvobZMhnoMv7h+8lIoXl7H9/SGz1Z0lC+9nkMUcCG/BAB\n" +
                "zx74Ul5R20RIEHlp3g1Ho/8ApfpzbUeF9lD64wzOiSngpdWeMdG+GeddJMyLhLSaYvsal3o94KFO\n" +
                "PuwPJbeo8zQHaBVnrf42KxJgVG4l0UrkyCTmldM9GBE+arglmMWhQcyuD8rDjSwcnm/t3dYnbh4B\n" +
                "Y95ARPq7wwwlRABy5WawGsKBEJYxje9D7wqPhMq3cMZ6mz7v0EQKCVcM5hLOSTnISMoZwiQICbBp\n" +
                "Jk0UWuBGqWUqaJwLgYgA+wPWcGTJL1D2H1xCuDmncA5dSeMUT9ZWrucS7a9cRBdUDHgZZ6R6nDFA\n" +
                "STIRLuIIGdODHCjCsHYdXewz5yni3OB2n910msheSVEO4RHSIiOk/EYe9X4lEgHCdh9dZNnGLYKI\n" +
                "XLYsOMkzMfsjAi09Brb9vIhF4j+n7fSIbduw/ccQlPBkAyHR1iycOqZ3pnoONebHCYS6DEz6YYuC\n" +
                "AvhKl4e4YRFGsTRMCgsipMYB2eaBEqKI70mA541LO8G/0YOP4GQ8jgyzYFjyqPWEYjNLN44VufXy\n" +
                "wMM47TdBmXbDw5EOpSLzwviuwY6l7mS7fXrR04V1SNEbJuuf/bEDrThJegG9uqy65qEbRFJKOJj8\n" +
                "I40gAySbNXM6i8iLAJkEUIQBj+WdKhCA2wsGCDKEElhBYkTPin7ZDs2+QkPVH6XPsSFC/wAB5wTI\n" +
                "hfSdbD+PH0N0T8A19iwhcegg1bQtALaZjRiYTo9oK2wGdO6cUKng0gWFEiNPpihJXSZcMMAXFkkB\n" +
                "KV3u4CAAgHYm8N5MGQdI7xrddudqfBthchQ5eBtHA9nIKWIt+H/2ztjUVJYH1DzHlPJWKehkD/gS\n" +
                "eydjgZtQSyxNILWlUdWIiJITsThGk4T8Dh/xKQCTNCtdZPFQAInMILDEjHJkgmaCiy8jQMihGKIi\n" +
                "ED5uSq+0ZNq/bGewh7fQWRhRs1iZrLzPibPeTHV4Ebblox8vpzjKABLukcG4ieYxdR7DQ+XA6vi5\n" +
                "+OYlNUDoaJTgpJ15MQAjdcypgQQFXYRXrVJCpQlbaO+K2KoQSkAbESexEEQwNZ4i9I8nybMWPuuf\n" +
                "+ZbHSYky3t5tf7g9EEIYjbTp6k7e70x4FQJI3E2Hwh7yaygwBRkKERXyrf4FhQRCh9SgEvRg03Ww\n" +
                "BWEheLYjW31EElSmSqy9mTHM9i6kqBZXpu/rBXu/XOfpjIX6QtSgSFEZBiKnAhMJorAT5axYt5aL\n" +
                "kItV7gxSBSQYO1A9FnQ8zr9IfZxJLmo15VweifGKriRL2oDHZGQIWigkDg2SFE8GgAKMJqQjsRD3\n" +
                "axfKFr6gLKSTbFVhV6DYJa3QQSI1rN73ur5ke+B9uVq5eo04Qvb6SCYRUbk8m12YwlG0klg6SHwm\n" +
                "PHfhbFo6UX0TBI1PXkqPun1x9GiYqiCBUcYIbTKIA23tPt+DP/uu2RphYmrfbf2wBNEygifYj5fs\n" +
                "ckKBJExDivViz8QuEpS7QS4xiRZyj87Ftf0ygGQbVDCkiv8AzAsvBBleT+xnwKY+X+mIO/yH1A+I\n" +
                "w7Lyh2bV2rytv0Vp5ja2wYl9r4wYbiFlED4D/sON3vIBSsyCSS2UTQY4IQNAGxtiBc+q9yndbm1s\n" +
                "Qjk8fY9K7YEB7TxwTf8AkzBXSV90uTgNAxgrG1lBI9HGnWJYrMSpXb8/e6DYwsT6SMTPjFoZFw2A\n" +
                "Wmd0uGvJyZAGrxTR375A4synr4Ame5I5j6zJBKAXaf3LCoL8Oftv7YSO9QP7P7iJTGwAisWXUXUw\n" +
                "4VjEKS2h4bnmi7+kYtSAdq4uNbbAGQ6gfRaeIRlwAMUcHnARkmOIaoSMmSe0Yw+Bp8sXcFMHUKOX\n" +
                "L+yqtLYoVgLxIi4vFYSQEhIyTbeRs7I+rjAY98JKkAUX6mI+pOWDDBCVGgOcQph9ZES9lyRHxZC7\n" +
                "ezK0YmlWSfJwJDErQleKE2ScJ+AmUh12eomG7h3WGCqUWCYFrDfF4LdH44pnwQVI6dYkk1iJ6mUC\n" +
                "N19zwSMtopbgerLh5x/qqsBRMJEjBJwYF0SOhLONJajCUgrOzRpWN2EWgYMSTImF7DA+Me1KRxpH\n" +
                "yAPkws7KuASvxgF8mgTuXswQKimSpHYhB9BMEuHAIVOpvVBoKLRBYAUNbbAVtvIQeQmLEuwGF6gO\n" +
                "yBSBMYpWTCDJyCJ0MNqMvXFeUc3rSkC19ZMrd2A6JeXNshqgCnwYx8S4xk/qZDT8PyleWMaSokDE\n" +
                "nRI+xhgwn8qEvdK+n4K75FEikrWIY06E5Amw9FjZG/OMSkFSggkKgnvJUdBZJsKQrzC9/aRSAwBW\n" +
                "oDTxbDbwIZAhpruxMRFya4nAoOQH4CtjwY+KADmltZJ9VjN9uFiEmXfLZMxEsyl30WyeUUnyD9DX\n" +
                "BuyQogWIixU4YTOupMozgQgDVSiUKwGE0x6vQ9oOQy4mxKk0TYPtm9ShlSK+CspJDrDyBcllQXV6\n" +
                "q6+Ahg4poB8BwVqccqE1nhGDb5AlQwTImB0T0eDwAErfODjRDROlQcHlXB+cICLDymiAiYwzrdYU\n" +
                "EWrAkDdmq/Ah+FlYRvRh6F6z/Y8caoiABUSyk3107wwTmax6yg0WE/aGwj6VUXyA8mQZhMC/ot9M\n" +
                "S2ihPNEGEWkogkGfWJWESgPDDllvFsh2CFENtXeDDISLCNNLvU5+hnVo4gkJJB77MGsK0RHjKdqW\n" +
                "FfqFUIOUbYbA+jQLg0J4UDcAbQmA74wKEiG8AtJOpb6yeSUCdumjCaLCRvCOhooLh274FixEZXQK\n" +
                "gJ+g4FHrailmYCFRsjLyA8RGChF168dR+EgXz3dqdNPuDzifDZQlKGRwEMbdRbY2jOWCDAaG5Lje\n" +
                "b0mdwQkqfT7AW6D0bKwnVHi0IqCEg5GcNuMIscyS2PI4mzegxtZS8fhiFUkLmNNawFFIMlKHIQPp\n" +
                "iPuDGoTC/FYiGYyOEJkz1iPmX0xjq84Bg5yC4VyJ2oF/pM/mblW0l6AP2ztkQK6kl84SHVQ47mCH\n" +
                "o4MyK5cgAIr4EwBjeoYQ9I09KPjBsQKojI1Jrok5w0gRFr6K8zBkrhkhIFfK0upIGSDiPYojqOPg\n" +
                "XhoI1YQ5uLs+z3YICCJInP4FxJu2JIUVoyTQ2hEYlRJ33DlRZhYMQDJo2TPLm9349EvVVRYT2/Ym\n" +
                "3WlPzAziVDSgQQtVBSlgC4wZVjCGPE8sLJyrhqtIzexKO4Zx56av64BwUAKMoZa7F/uFCAboIexk\n" +
                "MByNO0+YX94DB4yNnxE5yju+Sp9Vb4yeyLYa7SfJxrJOse9GPfC/ksHrwJUlk+jR/RiCOKwOgXAM\n" +
                "xbWS/SEMvc1s/GKLgb5rbuV/6LEeKOkDSOVCwuxF+M708jhRc7UoJbpFTDDyE/gNIHqpO2cV8rqA\n" +
                "QLHGsnU4g0SiFsiC8nckg0nCCH9fY5y42QP9xKROZn844mqDA6JIMTDWYiXZS9vo04/2JfQ5Pwem\n" +
                "b+7IxcOHJ+0R+84L8oXqgfjF1lH3pmf3k+sXCPlTEdMZBjdAsPQM2e9q9Bcs+MO45krAFWwLng7z\n" +
                "x+vjj6IqPLoZL+Iu1v0AwaoeeRHzOM8CcYU4JJpCB4kY8Y+aUR8mnxlbXTfcVJEkibl/BMWrE4yJ\n" +
                "0RyOJIuaE6UIqwrFNF5oUQHsGPVeMcP4yOeCW9vw+yPJt/UQcoh2k/hBlJGcfsIHD+nNxNz0j/25\n" +
                "zPRD+5gUjyv8ydeRCioQyTCwzFjwYDzkH2sZTDGAKNQyk0OP4A82DKH6xsioP5lhR02ndZbS7Z2n\n" +
                "CRSbSIYjWXfmPFEICkgR6vE4NUNClPC90XnWx9FHyP58Jq47QSEyUGMqrP8AJXXD8UQjK8gqVeDI\n" +
                "4vTAoBQlidwTF5OpVEXvn/GE3cwi1Dwqied1H4gNk8iASJGBO0lODEf0haP6/f6A08DUr10B8xgE\n" +
                "YBnUkx9GrUaclE9XKGZImKQj/wCYN8+mCR85qFkV+AXKIQ6TJX7CgCELTbBYJmowywBE0mRQeCix\n" +
                "sQpeciejBIuwFMbZX+/hcl7wNZSCaG0jGCU7CNGJhPflkxwEWMcKdcVrD2aJUjDcE4/EIQMQV7BD\n" +
                "QtvGTP1T1/EPzfQVHKIGEkkjCPhcoRKpsgEsBMF6PoDoZE5CPg/IzUJ0/QBR9Oj+PBB+y9+A4IVs\n" +
                "evnI/wDJimG8bTCvf3s4ggCJCPOJasR3a2vjblf4SZ1iNIUrJWb7yRq/uTg8POTO8p62WDyxqvVG\n" +
                "QGAF+PxME+06ezgC8kdNb9ZWOzrHEvnf/UtgpPq0cXhHPSf20Xj85toNOGmwuIlgysqBEr39K14n\n" +
                "IBkPKh75BHCUcxIfcvOiK67PlY9n0eAQE8tnqgrx+EaGFYIIEFlt5VvKBzRdLTYjACtEOgUYrHHO\n" +
                "VfJMk8GPZ8U/jjkIvgABDAYNJoNY6azFCEXEBMyTxnYtBAPANvZkAf021VS/1wFkhT1A7keVk4GO\n" +
                "PkwmqSBbYGJbY+yVKpHYCdw0mUP4LGDMUkkqELf4zhAAYAueSTMviIjBbAI1VBETbc+2VDWaJkrH\n" +
                "BesEESIiQTM3ZRFS/kBQEpESOpxrKAlQgAyKWLRJJjT7FDcREYIiCJ1h2oJ8H2pUTIU9B3F//cv/\n" +
                "2Q==";
        ImageHelper.createImg(poster, path);
    }
}