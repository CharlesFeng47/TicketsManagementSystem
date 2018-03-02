package cn.edu.nju.charlesfeng.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 为 Dao 层提供基本操作
 */
public interface BaseDao {

    /**
     * @param c  欲查找的实体对象的类型
     * @param id 欲查找的实体对象主键ID
     * @return 查找到的实体对象
     */
    Object get(Class c, Object id);

    /**
     * @param c 欲查找的实体对象的类型
     * @return 查找到的所有实体对象
     */
    List getAllList(Class c);

    /**
     * @param bean 欲保存的实体对象
     * @return 成功保存后的此实体对象主键
     */
    Serializable save(Object bean);

    /**
     * @param bean 欲更新的实体对象
     * @return 更新结果，成功则true
     */
    boolean update(Object bean);

    /**
     * @param c  欲删除的实体对象的类型
     * @param id 欲删除的实体对象主键ID
     * @return 删除结果，成功则true
     */
    boolean delete(Class c, Object id);

}
