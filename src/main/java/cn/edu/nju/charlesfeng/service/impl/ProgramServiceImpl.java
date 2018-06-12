package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.ProgramRepository;
import cn.edu.nju.charlesfeng.repository.TicketRepository;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.exceptions.ProgramNotSettlableException;
import cn.edu.nju.charlesfeng.util.filter.ProgramBrief;
import cn.edu.nju.charlesfeng.util.helper.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    private final TicketRepository ticketRepository;

    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, TicketRepository ticketRepository) {
        this.programRepository = programRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * 获取首页的每种类型的5个推荐节目
     *
     * @return 推荐列表
     */
    @Override
    public Map<String, List<Program>> recommendPrograms(LocalDateTime localDateTime, String city, int num) {
        //long start = System.currentTimeMillis();
        ProgramType types[] = ProgramType.values();
        Map<String, List<Program>> result = new TreeMap<>();
        for (ProgramType programType : types) {
            if (programType.equals(ProgramType.ALL)) {
                continue;
            }
            Sort sort = new Sort(Sort.Direction.ASC, "programID.startTime");
            Pageable pageable = new PageRequest(1, num, sort);
            //long start1 = System.currentTimeMillis();
            Page<Program> program_page = programRepository.getAvailablePrograms(localDateTime, programType, city, pageable);
            //System.out.println(programType+"第一次取"+(System.currentTimeMillis()-start1));
            List<Program> programs = new ArrayList<>(program_page.getContent());
            AddressHelper addressHelper = new AddressHelper();
            List<String> cities = addressHelper.getNearCity(city);
            if (programs.size() < num) {
                //long start2 = System.currentTimeMillis();
                for (String near_city : cities) {
                    Pageable pageable_need = new PageRequest(1, (num - programs.size()), sort);
                    Page<Program> program_page_need = programRepository.getAvailablePrograms(localDateTime, programType, near_city, pageable_need);
                    programs.addAll(program_page_need.getContent());

                    if (programs.size() == num) {
                        break;
                    }
                }
                //System.out.println(programType+"第二次取"+(System.currentTimeMillis()-start2));
            }

            if (programs.size() == num) {
                result.put(programType.toString(), programs);
            }
        }
        //System.out.println(System.currentTimeMillis() - start);
        return result;
    }

    /**
     * 按类型获取节目
     *
     * @param city          制定城市
     * @param programType   节目类型
     * @param localDateTime 指定时间
     * @return 节目列表
     */
    @Override
    public List<ProgramBrief> getBriefPrograms(String city, ProgramType programType, LocalDateTime localDateTime) {
        List<Program> programs = programRepository.getAvailablePrograms(localDateTime, programType, city);
        List<ProgramBrief> result = new ArrayList<>();
        for (Program program : programs) {
            int judge = ticketRepository.hasTickets(program.getProgramID());
            SaleType type = SaleType.TICKETING;
            if (judge == 0) {
                type = SaleType.REPLACEMENTTICKETING;
            }
            ProgramBrief programBrief = new ProgramBrief(program, type);
            result.add(programBrief);
        }
        return result;
    }

    /**
     * @return 所有日程
     */
    @Override
    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    /**
     * @return 用户可见的所有日程（即不含已过期的日程计划）
     */
    @Override
    public List<Program> getAllAvailablePrograms() {
        return null;
    }

    /**
     * @param venueID 欲获取的相关场馆的场馆Id
     * @return 相关场馆的所有日程
     */
    @Override
    public List<Program> getProgramsOfOneVenue(int venueID) {
        return programRepository.findByVenueID(venueID);
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
     * @param program 欲发布的活动日程描述
     * @return 是否成功发布，成功则为该实体对象
     */
    @Override
    public Program publishProgram(Program program) {
        //TODO  添加Program  并根据venue生成对应票（前置条件venue已添加至program中）
        return null;
    }

    /**
     * @param program 欲修改的活动日程描述
     * @return 是否成功修改，成功则true
     */
    @Override
    public boolean modifyProgram(Program program) {
        programRepository.save(program);
        return true;
    }

    /**
     * @param programID 要结算的计划ID
     * @return 结算结果，成功则true
     */
    @Override
    public boolean settleOneProgram(ProgramID programID) throws ProgramNotSettlableException {
        return false;
    }
}
