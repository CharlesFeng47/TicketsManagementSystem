package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数据层对节目的操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface ProgramRepository extends JpaRepository<Program, ProgramID> {
}
