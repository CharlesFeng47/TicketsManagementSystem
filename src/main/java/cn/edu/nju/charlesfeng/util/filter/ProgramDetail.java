package cn.edu.nju.charlesfeng.util.filter;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.util.enums.SaleType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 节目概览
 */
public class ProgramDetail implements Serializable {

    /**
     * 界面需要的ID定位
     */
    private String id;

    /**
     * 节目开始时间
     */
    private LocalDateTime time;

    /**
     * 节目名称
     */
    private String programName;

    /**
     * 节目类型
     */
    private String programType;

    /**
     * 浏览量
     */
    private int scanVolume;

    /**
     * 喜爱量
     */
    private int favoriteVolume;

    /**
     * 当前节目的售票状态
     */
    private String saleType;

    /**
     * 场次
     */
    private Set<LocalDateTime> fields;

    /**
     * 票面
     */
    private Set<Par> pars;

    /**
     * 场馆
     */
    private Venue venue;

    /**
     * 海报
     */
    private String poster;

    /**
     * 剩余票的数量
     */
    private int remainTicketsNumber;

    public ProgramDetail(Program program, SaleType type, Set<LocalDateTime> fields, int remianNumber) {
        id = String.valueOf(program.getProgramID().getVenueID()) + ";" + program.getProgramID().getStartTime().toString();
        poster = program.getPoster();
        programName = program.getName();
        time = program.getProgramID().getStartTime();
        venue = program.getVenue();
        scanVolume = program.getScanVolume();
        favoriteVolume = program.getFavoriteVolume();
        saleType = type.toString();
        this.fields = fields;
        pars = program.getPars();
        programType = program.getProgramType().toString();
        remainTicketsNumber = remianNumber;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramType() {
        return programType;
    }

    public int getScanVolume() {
        return scanVolume;
    }

    public int getFavoriteVolume() {
        return favoriteVolume;
    }

    public String getSaleType() {
        return saleType;
    }

    public Set<LocalDateTime> getFields() {
        return fields;
    }

    public Set<Par> getPars() {
        return pars;
    }

    public Venue getVenue() {
        return venue;
    }

    public String getPoster() {
        return poster;
    }

    public int getRemainTicketsNumber() {
        return remainTicketsNumber;
    }
}
