package cn.edu.nju.charlesfeng.util.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SowingHelper {

    private SowingHelper() {
    }

    /**
     * 根据城市获取轮播图的ID
     *
     * @param city 城市
     * @return ID
     * @throws IOException 文件读取异常
     */
    public static List<String> readAreaSowingID(String city) throws IOException {
        String file = convertName(city) + ".txt";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(file)));
        String line = null;
        List<String> result = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    /**
     * 把城市转换为对应的文件名
     *
     * @param city 城市
     * @return 文件名
     */
    private static String convertName(String city) {
        switch (city) {
            case "上海":
                return "shanghai";
            case "南京":
                return "nanjing";
        }
        return null;
    }
}
