package cn.edu.nju.charlesfeng.dto.program;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * 节目概览
 *
 * @author Dong
 */
public class ProgramBriefDTO implements Serializable {

    /**
     * 界面需要的ID定位
     */
    private String id;

    /**
     * 节目开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    /**
     * 节目名称
     */
    private String programName;

    /**
     * 节目介绍
     */
    private String description;

    /**
     * 最低价
     */
    private Double lowPrice;

    /**
     * 界面所在城市
     */
    private String city;

    /**
     * 场馆名
     */
    private String venueName;

    /**
     * 浏览量
     */
    private Integer scanVolume;

    /**
     * 喜爱量
     */
    private Integer favoriteVolume;

    /**
     * 当前节目的售票状态
     */
    private String saleType;

    /**
     * 海报的图片url
     */
    private String imageUrl;

    public ProgramBriefDTO(Program program) {
        init(program);
    }

    public ProgramBriefDTO(Program program, SaleType type) {
        init(program);
        saleType = type.toString();

    }

    private void init(Program program) {
        id = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programName = program.getName();
        description = program.getDescription();
        time = program.getProgramID().getStartTime();
        Venue venue = program.getVenue();
        city = venue.getAddress().getCity();
        venueName = venue.getVenueName();
        scanVolume = program.getScanVolume();
        favoriteVolume = program.getFavoriteVolume() + program.getUsers().size();
        imageUrl = SystemHelper.getDomainName() + program.getProgramType().name() + "/" + id + ".jpg";
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

    public String getProgramName() {
        return programName;
    }

    public String getDescription() {
        return description;
    }

    public Double getLowPrice() {
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

    public Integer getScanVolume() {
        return scanVolume;
    }

    public Integer getFavoriteVolume() {
        return favoriteVolume;
    }

    public String getSaleType() {
        return saleType;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
