package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

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
        OrderID orderID = new OrderID();
        orderID.setEmail("151250032@smail.nju.edu.cn");
        orderID.setTime(LocalDateTime.of(2017,5,4 ,4,50,51));
        Order order = orderRepository.findByOrderID(orderID);
        Hibernate.initialize(order);
        System.out.println(order.getTickets().size());

    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testAddOrder() {
        List<String> users = Arrays.asList("151250032@smail.nju.edu.cn", "151250037@smail.nju.edu.cn", "151250040@smail.nju.edu.cn", "151250043@smail.nju.edu.cn");
        List<Program> programList = programRepository.getBeforeProrgam("上海", LocalDateTime.of(2018, 5, 1, 0, 0, 0));
        for (int i = 0; i < 30; i++) {
            Program program = programList.get(randomIndex(programList.size()));
            List<Ticket> tickets = ticketRepository.getTickets(program.getProgramID(), false);
            String userID = users.get(randomIndex(users.size()));
            Ticket ticket = tickets.get(randomIndex(tickets.size()));
            LocalDateTime end = program.getProgramID().getStartTime();
            LocalDateTime start = end.minusMonths(3);
            LocalDateTime order_time = randomeLocalDateTime(start, end);
            OrderID orderID = new OrderID();
            orderID.setEmail(userID);
            orderID.setTime(order_time);
            Order testOrder = orderRepository.findByOrderID(orderID);
            while (testOrder != null) {
                orderID.setTime(randomeLocalDateTime(start, end));
                testOrder = orderRepository.findByOrderID(orderID);
            }
            Order order = new Order();
            order.setOrderID(orderID);
            order.setProgramID(program.getProgramID());
            order.setOrderState(OrderState.PAYED);
            order.setProgram(program);
            order.setTotalPrice(ticket.getPrice());
            Set<Ticket> tickets1 = new HashSet<>();
            ticket.setLock(true);
            ticket.setOrder(order);
            tickets1.add(ticket);
            order.setTickets(tickets1);
            orderRepository.save(order);
            System.out.println(order.getOrderID().getEmail() + "-------" + order.getOrderID().getTime().toString());
        }
    }


    private int randomIndex(int max) {
        int index = (int) (Math.random() * max);
        if (index < 0) {
            index = 0;
        }
        if (index >= max) {
            index = max - 1;
        }
        return index;
    }

    private LocalDateTime randomeLocalDateTime(LocalDateTime start, LocalDateTime end) {
        //LocalDateTime to Date
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt_start = start.atZone(zoneId);
        ZonedDateTime zdt_end = end.atZone(zoneId);
        Date start_date = Date.from(zdt_start.toInstant());
        Date end_date = Date.from(zdt_end.toInstant());

        //根据long进行random日期
        long start_time = start_date.getTime();
        long end_time = end_date.getTime();
        long rtn = start_time + (long) (Math.random() * (end_time - start_time));

        // 把得到的random long  转化为Date -> 转化为LocalDateTime
        Date result = new Date(rtn);
        Instant instant = result.toInstant();
        ZoneId zoneId_result = ZoneId.systemDefault();

        return instant.atZone(zoneId_result).toLocalDateTime();
    }
}