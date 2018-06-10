package cn.edu.nju.charlesfeng.util.filter;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * 节目概览
 */
public class BriefProgram implements Serializable {

    /**
     * 界面需要的ID定位
     */
    private String id;

    /**
     * 海报
     */
    private String poster;

    /**
     * 节目名称
     */
    private String programName;

    /**
     * 最低价
     */
    private double lowPrice;

    /**
     * 界面所在城市
     */
    private String city;

    /**
     * 场馆名
     */
    private String venueName;

    /**
     * 节目开始时间
     */
    private LocalDateTime time;

    public BriefProgram(Program program) {
        id = String.valueOf(program.getProgramID().getVenueID()) + ";" + program.getProgramID().getStartTime().toString();
        poster = program.getPoster();
        programName = program.getName();
        time = program.getProgramID().getStartTime();
        Venue venue = program.getVenue();
        city = venue.getAddress().getCity();
        venueName = venue.getVenueName();
        Iterator<Par> iterator = program.getPars().iterator();
        lowPrice = iterator.next().getParID().getBasePrice();
        while (iterator.hasNext()) {
            Par par = iterator.next();
            if (Double.doubleToLongBits(par.getParID().getBasePrice()) < Double.doubleToLongBits(lowPrice)) {
                lowPrice = par.getParID().getBasePrice();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getProgramName() {
        return programName;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public String getCity() {
        return city;
    }

    public String getVenueName() {
        return venueName;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
