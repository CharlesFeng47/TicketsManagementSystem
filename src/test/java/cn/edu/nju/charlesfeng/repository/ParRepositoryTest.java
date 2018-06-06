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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
        par1.setComments("优惠");
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
        par2.setComments("优惠");
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