package cn.edu.nju.charlesfeng.dao;

import cn.edu.nju.charlesfeng.entity.Consumption;

import java.util.List;

/**
 * 数据层对消费记录的操作
 */
public interface ConsumptionDao {

    /**
     * @param id 要查看的消费记录ID
     * @return 该消费记录实体
     */
    Consumption getConsumption(int id);

    /**
     * @return 所有的消费记录实体
     */
    List<Consumption> getAllConsumption();

    /**
     * @param consumption 欲保存的消费记录实体
     * @return 成功保存后的此实体对象主键
     */
    int saveConsumption(Consumption consumption);
}
