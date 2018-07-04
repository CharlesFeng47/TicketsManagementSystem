package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ParRepository parRepository;

    @Test
    public void testAdd() {
        Venue venue = venueRepository.getOne(1);
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        Program program = programRepository.getOne(programID);
        Set<Seat> seats = venue.getSeats();
        Iterator<Seat> iterator = seats.iterator();
        Set<Ticket> tickets = new HashSet<>();
        while (iterator.hasNext()) {
            Seat seat = iterator.next();
            Ticket ticket = new Ticket();
            TicketID ticketID = new TicketID();
            ticketID.setProgramID(programID);
            ticketID.setCol(seat.getSeatID().getCol());
            ticketID.setRow(seat.getSeatID().getRow());
            ticket.setTicketID(ticketID);
            if (seat.getType().equals("A区")) {
                ticket.setPrice(200);
            } else {
                ticket.setPrice(200);
            }
            ticket.setLock(false);
            ticket.setProgram(program);
            ticketRepository.save(ticket);
            tickets.add(ticket);
        }
        program.setTickets(tickets);
        programRepository.save(program);
    }

    @Test
    public void testModify() {
    }

    @Test
    public void testGet() {

    }

    @Test
    public void testDelete() {
    }

    @Test
    //@Rollback
    public void testAddAll() {
        List<Program> programs = programRepository.findAll();
        for (Program program : programs) {
            System.out.println("----------------------------------------");
            System.out.println("开始生成节目票：" + program.getName());
            Venue venue = program.getVenue();
            Set<Seat> seats = venue.getSeats();
            if (seats.isEmpty()) {
                System.out.println(venue.getVenueID() + "--" + venue.getVenueName());
                System.out.println("该节目为空");
            }
            Iterable<Ticket> tickets = new HashSet<>();
            for (Seat seat : seats) {
                Ticket ticket = new Ticket();
                TicketID ticketID = new TicketID();
                ticketID.setProgramID(program.getProgramID());
                ticketID.setRow(seat.getSeatID().getRow());
                ticketID.setCol(seat.getSeatID().getCol());
                ticket.setLock(false);
                ticket.setTicketID(ticketID);
                ticket.setProgram(program);
                ticket.setPrice(parRepository.findPrice(program.getProgramID(), seat.getType()));
                ((HashSet<Ticket>) tickets).add(ticket);
                System.out.println(ticket.getTicketID().getRow() + "--" + ticket.getTicketID().getCol() + "--" + ticket.getPrice());
            }

            //seats.clear();
            ticketRepository.saveAll(tickets);
            program.setTickets((Set<Ticket>) tickets);
            programRepository.save(program);
        }

    }

    @Test
    //@Rollback
    public void testAddNewProgramTicket() {
        List<ProgramID> programIDS = readUnGetProgram();
        for (ProgramID programID : programIDS) {
            Program program = programRepository.findByProgramID(programID);
            System.out.println("----------------------------------------");
            System.out.println("开始生成节目票：" + program.getName());
            Venue venue = program.getVenue();
            Set<Seat> seats = venue.getSeats();
            if (seats.isEmpty()) {
                System.out.println(venue.getVenueID() + "--" + venue.getVenueName());
                System.out.println("该节目为空");
            }
            System.out.println(program.getProgramID().getVenueID()+"-"+program.getProgramID().getStartTime().toString());
            Iterable<Ticket> tickets = new HashSet<>();
            for (Seat seat : seats) {
                Ticket ticket = new Ticket();
                TicketID ticketID = new TicketID();
                ticketID.setProgramID(program.getProgramID());
                ticketID.setRow(seat.getSeatID().getRow());
                ticketID.setCol(seat.getSeatID().getCol());
                ticket.setLock(false);
                ticket.setTicketID(ticketID);
                ticket.setProgram(program);
                Double price = parRepository.findPrice(program.getProgramID(), seat.getType());
                if(price == null){
                    System.out.println("not find:"+program.getProgramID().getVenueID()+"-"+program.getProgramID().getStartTime().toString()+seat.getType());
                }
                ticket.setPrice(price);
                ticket.setSeatType(seat.getType());
                ((HashSet<Ticket>) tickets).add(ticket);
                System.out.println(ticket.getTicketID().getRow() + "--" + ticket.getTicketID().getCol() + "--" + ticket.getPrice());
            }
            //seats.clear();
            ticketRepository.saveAll(tickets);
            program.setTickets((Set<Ticket>) tickets);
            programRepository.save(program);
        }

    }

    @Test
    public void testModifyType() {

    }

    @Test
    public void lock() {

    }

    public List<ProgramID> readUnGetProgram() {
        String path = "C:\\Users\\Byron Dong\\Desktop\\2.txt";
        File file = new File(path);
        List<ProgramID> programIDS = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String info[] = line.split(";");
                ProgramID programID = new ProgramID();
                programID.setVenueID(Integer.parseInt(info[0]));
                programID.setStartTime(LocalDateTime.parse(info[1]));
                programIDS.add(programID);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return programIDS;
    }
}