package cn.edu.nju.charlesfeng.dto.program;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * 节目概览
 *
 * @author Dong
 */
public class ProgramDetailDTO implements Serializable {

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
     * 节目类型
     */
    private ProgramType programType;

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
    private SaleType saleType;

    /**
     * 场次
     */
    private Set<LocalDateTime> fields;

    /**
     * 票面ID集合
     */
    private Set<ParDTO> parIDs;

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
    private Integer remainTicketsNumber;

    /**
     * 海报的图片url
     */
    private String imageUrl;

    public ProgramDetailDTO(Program program, SaleType type, Set<LocalDateTime> fields, int remainNumber) {
        id = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programName = program.getName();
        time = program.getProgramID().getStartTime();
        venueName = program.getVenue().getVenueName();
        address = program.getVenue().getAddress().toString();
        scanVolume = program.getScanVolume();
        favoriteVolume = program.getFavoriteVolume() + program.getUsers().size();
        imageUrl = SystemHelper.getDomainName() + program.getProgramType().name() + "/" + id + ".jpg";
        saleType = type;
        this.fields = fields;
        parIDs = new TreeSet<>();
        for (Par par : program.getPars()) {
            parIDs.add(new ParDTO(par));
        }
        programType = program.getProgramType();
        remainTicketsNumber = remainNumber;
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

    public ProgramType getProgramType() {
        return programType;
    }

    public Integer getScanVolume() {
        return scanVolume;
    }

    public Integer getFavoriteVolume() {
        return favoriteVolume;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public Set<LocalDateTime> getFields() {
        return fields;
    }

    public Set<ParDTO> getParIDs() {
        return parIDs;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getRemainTicketsNumber() {
        return remainTicketsNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
