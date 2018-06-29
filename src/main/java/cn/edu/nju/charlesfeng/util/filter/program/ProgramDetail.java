package cn.edu.nju.charlesfeng.util.filter.program;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ParID;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
     * 票面ID集合
     */
    private Set<ParDto> parIDs;

    /**
     * 场馆名
     */
    private String venueName;

    /**
     * 场馆地址
     */
    private String address;

    /**
     * 剩余票的数量
     */
    private int remainTicketsNumber;

    /**
     * 当前登陆用户对该节目是否喜爱
     */
    private boolean isLike;

    /**
     * 海报的图片url
     */
    private String imageUrl;

    public ProgramDetail(Program program, SaleType type, Set<LocalDateTime> fields, int remainNumber, boolean isLike) {
        id = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programName = program.getName();
        time = program.getProgramID().getStartTime();
        venueName = program.getVenue().getVenueName();
        address = program.getVenue().getAddress().toString();
        scanVolume = program.getScanVolume();
        favoriteVolume = program.getFavoriteVolume() + program.getUsers().size();
        imageUrl = SystemHelper.getDomainName() + program.getProgramType().name() + "/" + id + ".jpg";
        saleType = type.toString();
        this.fields = fields;
        parIDs = new TreeSet<>();
        for (Par par : program.getPars()) {
            parIDs.add(new ParDto(par));
        }
        programType = program.getProgramType().toString();
        remainTicketsNumber = remainNumber;
        this.isLike = isLike;
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

    public Set<ParDto> getParIDs() {
        return parIDs;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getAddress() {
        return address;
    }

    public int getRemainTicketsNumber() {
        return remainTicketsNumber;
    }

    public boolean isLike() {
        return isLike;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
