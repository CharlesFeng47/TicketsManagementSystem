package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.UnexaminedSpot;

import java.util.Set;

/**
 * 提供给系统中经理的服务
 */
public interface ManagerService {

    /**
     * 获取所有未被审批的场馆申请（各场馆注册、修改场馆信息）
     *
     * @return 所有未被审批的场馆申请
     */
    Set<UnexaminedSpot> getAllUnexaminedSpotApplications();

    /**
     * 审批各场馆注册/修改场馆信息的申请
     * TODO
     *
     * @return 审批结果，成功则true
     */
    boolean examine();

    /**
     * 将会员支付结算给各场馆
     *
     * @return 结算结果，成功则true
     */
    boolean settle();
}
