package cn.edu.nju.charlesfeng.util.helper;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 用户读取存储图片并转换图片编码
 */
public class ImgHelper {

    private ImgHelper() {
    }

    /**
     * 获取base64编码的图片流
     *
     * @param path 图片路径
     * @return 图片流
     */
    public static String getBaseImg(String path) {
        File img = new File(path);
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(img);
            data = new byte[in.available()];
            int result = in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        assert data != null;
        return encoder.encode(data);
    }

    /**
     * 获取图片的base64编码，并生成图片到指定路径
     *
     * @param img  图片流
     * @param path 需要生成的图片路径
     */
    public static void createImg(String img, String path) {
        if (img == null) {//图像数据为空
            return;
        }

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
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
