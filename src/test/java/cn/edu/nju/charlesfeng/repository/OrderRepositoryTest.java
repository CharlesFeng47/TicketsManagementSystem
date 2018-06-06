package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void testAdd() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        Program program = programRepository.getOne(programID);
        Order order = new Order();
        OrderID orderID = new OrderID();
        orderID.setEmail("1234567890@qq.com");
        orderID.setTime(LocalDateTime.now());
        order.setOrderID(orderID);
        order.setProgram(program);
        order.setTotalPrice(200);
        Set<Ticket> tickets = program.getTickets();
        Ticket ticket = tickets.iterator().next();

        ticket.setLock(true);
        Set<Ticket> ticketList = new HashSet<>();
        ticketList.add(ticket);
        order.setTickets(ticketList);
        order.setOrderState(OrderState.PAYED);
        orderRepository.save(order);
    }

    @Test
    public void testModify() {

    }

    @Test
    public void testGet() {
//        ProgramID programID = new ProgramID();
//        programID.setVenueID(1);
//        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
//        Program program = programRepository.getOne(programID);
        OrderID orderID = new OrderID();
        orderID.setEmail("1234567890@qq.com");
        orderID.setTime(LocalDateTime.of(2018,6,6 ,17,0,40));
        Order order = orderRepository.getOne(orderID);
//        Set<Ticket> tickets = program.getTickets();
//        Ticket ticket = tickets.iterator().next();
//
//        ticket.setLock(true);
//        ticket.setOrder(order);
//        ticketRepository.save(ticket);
        System.out.println(order.getTickets().size());

    }

    @Test
    public void testDelete() {
    }


}