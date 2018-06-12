package cn.edu.nju.charlesfeng.util.filter;

import cn.edu.nju.charlesfeng.model.id.ProgramID;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用于封装预搜索的结果
 */
public class PreviewSearchResult implements Serializable {

    private ProgramID programID;

    private String programName;

    public PreviewSearchResult(Object[] program) {
        this.programID = (ProgramID) program[0];
        this.programName = String.valueOf(program[1]);
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreviewSearchResult that = (PreviewSearchResult) o;
        return Objects.equals(programID, that.programID) &&
                Objects.equals(programName, that.programName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(programID, programName);
    }
}
