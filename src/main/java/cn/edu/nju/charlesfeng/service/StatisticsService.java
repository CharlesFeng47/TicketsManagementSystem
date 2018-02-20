package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.MemberStatistics;
import cn.edu.nju.charlesfeng.model.SpotStatistics;

/**
 * 为系统提供统计服务
 */
public interface StatisticsService {

    /**
     * @param oid 要查看的会员
     * @return 会员本人的统计信息（预定、退订、消费等）
     */
    MemberStatistics checkMyMemberStatistics(String oid);

    /**
     * @param sid 要查看的场馆ID
     * @return 本场馆的统计信息（预订、退订、财务等）
     */
    SpotStatistics checkMySpotStatistics(String sid);
}
