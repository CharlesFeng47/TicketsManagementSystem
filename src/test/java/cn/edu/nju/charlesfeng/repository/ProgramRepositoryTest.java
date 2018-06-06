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
        program.setLikeVolume(200);
        program.setName("YIYA NO");
        program.setProgramType(ProgramType.CONCERT);
        program.setScanVolume(23456);
        program.setPoster(getBaseImg());
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

    private String getBaseImg() {
        File img = new File("F:\\img.jpg");
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


}