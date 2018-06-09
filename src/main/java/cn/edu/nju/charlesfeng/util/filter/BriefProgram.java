package cn.edu.nju.charlesfeng.util.filter;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

public class BriefProgram implements Serializable {

    private String poster;

    private String programName;

    private double lowPrice;

    private String city;

    private String venueName;

    private LocalDateTime time;

    public BriefProgram(Program program) {
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
