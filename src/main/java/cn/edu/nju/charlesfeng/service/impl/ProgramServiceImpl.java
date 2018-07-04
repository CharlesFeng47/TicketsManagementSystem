package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dto.program.PreviewSearchResultDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramBriefDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramDetailDTO;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.ProgramRepository;
import cn.edu.nju.charlesfeng.repository.TicketRepository;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.exceptions.venue.ProgramNotSettlableException;
import cn.edu.nju.charlesfeng.util.helper.AddressHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    private final TicketRepository ticketRepository;

    private final TicketServiceImpl ticketService;

    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, TicketRepository ticketRepository, TicketServiceImpl ticketService) {
        this.programRepository = programRepository;
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
    }

    /**
     * 获取首页的每种类型的5个推荐节目
     *
     * @return 推荐列表
     */
    @Override
    public Map<String, List<ProgramBriefDTO>> recommendPrograms(String city, int num) {
        long start = System.currentTimeMillis();
        ProgramType types[] = ProgramType.values();
        Map<String, List<ProgramBriefDTO>> result = new TreeMap<>();
        for (ProgramType programType : types) {
            if (programType.equals(ProgramType.ALL)) {
                continue;
            }

            long start1 = System.currentTimeMillis();
            List<ProgramBriefDTO> programs = this.getAvailablePrograms(programType, city, num);
            System.out.println(programType + "第一次取" + (System.currentTimeMillis() - start1));
            AddressHelper addressHelper = new AddressHelper();
            List<String> cities = addressHelper.getNearCity(city);
            if (programs.size() < num) {
                long start2 = System.currentTimeMillis();
                for (String near_city : cities) {
                    List<ProgramBriefDTO> program_need = this.getAvailablePrograms(programType, near_city, num);
                    programs.addAll(program_need);
                    if (programs.size() == num) {
                        break;
                    }
                }
                System.out.println(programType + "第二次取" + (System.currentTimeMillis() - start2));
            }

            if (programs.size() == num) {
                result.put(programType.toString(), programs);
            }
        }
        System.out.println(System.currentTimeMillis() - start);
        return result;
    }

    /**
     * 按类型获取节目
     *
     * @param city        制定城市
     * @param programType 节目类型
     * @return 节目列表
     */
    @Override
    public List<ProgramBriefDTO> getBriefPrograms(String city, ProgramType programType) {
        List<Object[]> programs = programRepository.getAvailableProgramIds(ProgramType.getIndex(programType), city);
        List<ProgramBriefDTO> result = new ArrayList<>();
        for (Object[] id : programs) {
            ProgramID programID = new ProgramID();
            programID.setVenueID((Integer) id[0]);
            programID.setStartTime(((Timestamp) id[1]).toLocalDateTime());
            int judge = ticketRepository.hasTickets(programID, false);
            SaleType type = SaleType.TICKETING;
            if (judge == 0) {
                type = SaleType.REPLACEMENTTICKETING;
            }
            Program program = programRepository.findByProgramID(programID);
            ProgramBriefDTO programBriefDTO = new ProgramBriefDTO(program, type);
            result.add(programBriefDTO);
        }
        return result;
    }

    /**
     * @param programID 欲获取的日程Id
     * @return 日程详情实体
     */
    @Override
    public Program getOneProgram(ProgramID programID) {
        return programRepository.findByProgramID(programID);
    }

    /**
     * 获取轮播图的节目真正的节目ID
     *
     * @param preProgramID 节目ID
     * @return 节目ID
     */
    @Override
    public String getSowingProgram(ProgramID preProgramID) {
        Object[] id = programRepository.getSowingProgramID(preProgramID.getVenueID(), preProgramID.getStartTime());
        return String.valueOf(id[0]) + "-" + String.valueOf(TimeHelper.getLong(((Timestamp) id[1]).toLocalDateTime()));
    }

    /**
     * 根据场馆ID，节目名获取所有场次
     *
     * @param venueID 场馆ID
     * @param name    节目名
     * @return 场次
     */
    @Override
    public Set<LocalDateTime> getAllProgramField(int venueID, String name) {
        return new TreeSet<>(programRepository.findField(venueID, name));
    }

    /**
     * 根据条件进行模糊搜索节目
     *
     * @param condition 条件
     * @return 节目简介列表
     */
    @Override
    public List<ProgramBriefDTO> search(String condition) {
        List<ProgramBriefDTO> result = new ArrayList<>();
        String conditions[] = null;
        if (condition.contains(" ")) {
            conditions = condition.split("\\s");
        } else {
            conditions = new String[]{condition};
        }

        long start1 = System.currentTimeMillis();
        for (String info : conditions) {
            System.out.println("--------------------------------------------------");
            List<Object[]> programIDS = programRepository.searchProgram("%" + info + "%");
            long start3 = System.currentTimeMillis();
            for (Object[] id : programIDS) {
                ProgramID programID = new ProgramID();
                programID.setVenueID((Integer) id[0]);
                programID.setStartTime(((Timestamp) id[1]).toLocalDateTime());
                Program program = programRepository.findByProgramID(programID);
                SaleType saleType = ticketService.getProgramSaleType(program.getProgramID());
                ProgramBriefDTO programBriefDTO = new ProgramBriefDTO(program, saleType);
                if (!result.contains(programBriefDTO)) {
                    result.add(programBriefDTO);
                }
            }
            System.out.println(System.currentTimeMillis() - start3);
            System.out.println("--------------------------------------------------");
        }
        System.out.println(System.currentTimeMillis() - start1);
        return result;
    }

    /**
     * 根据条件进行模糊预搜索节目
     *
     * @param condition 条件
     * @return 节目简介列表
     */
    @Override
    public List<PreviewSearchResultDTO> previewSearch(String condition, int result_num) {
        List<PreviewSearchResultDTO> result = new ArrayList<>();
        String conditions[] = null;
        if (condition.contains(" ")) {
            conditions = condition.split("\\s");
        } else {
            conditions = new String[]{condition};
        }

        for (String info : conditions) {
            List<Object[]> search_result = programRepository.previewSearchProgram("%" + info + "%");
            if (result.isEmpty()) {
                result.addAll(convert(search_result)); //结合初始为空时，取并集
            } else {
                result.retainAll(convert(search_result)); //取交集
            }
        }

        if (result.size() <= result_num) {
            return result;
        }

        return result.subList(0, result_num);
    }

    /**
     * 对节目的浏览量加1
     *
     * @param programID 节目ID
     */
    @Override
    public void addScanVolume(ProgramID programID) {
        programRepository.addOneScanVolume(programID.getVenueID(), programID.getStartTime());
    }

    /**
     * 用于将查询出来Object转化包装类，主要用于比较
     *
     * @param list 查询的结果
     * @return
     */
    private List<PreviewSearchResultDTO> convert(List<Object[]> list) {
        List<PreviewSearchResultDTO> result = new ArrayList<>();
        for (Object[] need : list) {
            result.add(new PreviewSearchResultDTO(need));
        }
        return result;
    }

    /**
     * 根据节目类型和城市获取节目
     *
     * @param programType 节目类型
     * @param city        城市
     * @return 节目概览
     */
    private List<ProgramBriefDTO> getAvailablePrograms(ProgramType programType, String city) {
        List<Object[]> programIDS = programRepository.getAvailableProgramIds(ProgramType.getIndex(programType), city);
        List<ProgramBriefDTO> result = new ArrayList<>();
        for (Object[] id : programIDS) {
            ProgramID programID = new ProgramID();
            programID.setVenueID((Integer) id[0]);
            programID.setStartTime(((Timestamp) id[1]).toLocalDateTime());
            Program program = programRepository.findByProgramID(programID);
            result.add(new ProgramBriefDTO(program));
        }
        return result;
    }

    /**
     * 根据节目类型和城市获取指定数量节目（节目随机）
     *
     * @param programType 节目类型
     * @param city        城市
     * @return 节目概览
     */
    private List<ProgramBriefDTO> getAvailablePrograms(ProgramType programType, String city, int page) {
        List<Object[]> programIDS = programRepository.getAvailableProgramIds(ProgramType.getIndex(programType), city);
        List<ProgramBriefDTO> result = new ArrayList<>();
        if (programIDS.size() <= page) {
            for (Object[] id : programIDS) {
                ProgramID programID = new ProgramID();
                programID.setVenueID((Integer) id[0]);
                programID.setStartTime(((Timestamp) id[1]).toLocalDateTime());
                Program program = programRepository.findByProgramID(programID);
                result.add(new ProgramBriefDTO(program));
            }
        } else {
            List<Integer> indexs = randomIndex(programIDS.size(), page);
            for (Integer index : indexs) {
                Object[] id = programIDS.get(index);
                ProgramID programID = new ProgramID();
                programID.setVenueID((Integer) id[0]);
                programID.setStartTime(((Timestamp) id[1]).toLocalDateTime());
                Program program = programRepository.findByProgramID(programID);
                result.add(new ProgramBriefDTO(program));
            }
        }
        return result;
    }

    /**
     * 根据size获取指定数量随机索引
     *
     * @param size 长度
     * @param num  数量
     * @return 指定数量的随机索引
     */
    private List<Integer> randomIndex(int size, int num) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int index = random(size);
            while (result.contains(index)) {
                index = random(size);
            }
            result.add(index);
        }
        return result;
    }

    /**
     * 根据size获取随机索引
     *
     * @param size 长度
     * @return 索引
     */
    private int random(int size) {
        int result = (int) (Math.random() * size);
        if (result < 0) {
            result = 0;
        }

        if (result >= size) {
            result = size - 1;
        }
        return result;
    }

}

