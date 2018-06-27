package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.*;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public void testModifyType() {

    }

    @Test
    public void lock() {

    }
}