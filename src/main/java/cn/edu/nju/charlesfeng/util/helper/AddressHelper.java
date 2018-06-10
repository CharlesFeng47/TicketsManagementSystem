package cn.edu.nju.charlesfeng.util.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于获取地址的最近的地址
 */
public class AddressHelper {

    /**
     * 上海及其附近的地点
     */
    private List<String> distance_s;

    /**
     * 南京及其附近的地点
     */
    private List<String> distance_n;

    public AddressHelper() {
        distance_s = Arrays.asList("上海", "苏州", "无锡", "昆山");
        distance_n = Arrays.asList("南京", "上海", "宜兴", "常熟");
    }

    /**
     * 给定地点，查找该地点的附近城市
     *
     * @param city 指定城市
     * @return 城市列表
     */
    public List<String> getNearCity(String city) {
        List<String> result = new ArrayList<>();
        List<String> findList = null;
        if (distance_n.contains(city)) {
            findList = distance_n;
        } else {
            findList = distance_s;
        }

        for (String need_city : findList) {
            if (need_city.equals(city)) {
                continue;
            }
            result.add(need_city);
        }
        return result;
    }
}
