package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.OrderRepository;
import cn.edu.nju.charlesfeng.repository.ProgramRepository;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.service.ParService;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ParService parService;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testControllerCreateOrder() {
        int num = 1;
        String seatType = "A区";
        String userID = "151250032@smail.nju.edu.cn";
        try {
            ProgramID programID = new ProgramID();
            programID.setVenueID(15);
            programID.setStartTime(LocalDateTime.of(2017, 6, 14, 0, 0, 0));
            List<Ticket> tickets = ticketService.lock(programID, num, seatType);  //进行锁票,后面加锁
            OrderID orderID = new OrderID();
            orderID.setTime(TimeHelper.standardTime(LocalDateTime.now()));
            System.out.println(TimeHelper.getLong(orderID.getTime()));
            orderID.setEmail(userID);
            Order order = new Order();
            order.setOrderID(orderID);
            double price = parService.getSeatPrice(programID, seatType);
            order.setTotalPrice(price * num);
            order.setOrderState(OrderState.UNPAID);
            for (Ticket ticket : tickets) {
                ticket.setOrder(order);
            }
            Program program = programService.getOneProgram(programID);
            program.getOrders().add(order);
            order.setProgram(program);
            order.setProgramID(program.getProgramID());
            order.setTickets(new HashSet<>(tickets)); //关联订单
            orderService.generateOrder(order);
        } catch (TicketsNotAdequateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelOrder() {
        try {
            String userID = "151250032@smail.nju.edu.cn";
            OrderID orderID = new OrderID();
            orderID.setTime(LocalDateTime.of(2018, 6, 28, 16, 26, 13));
            orderID.setEmail(userID);
            orderService.cancelOrder(orderID);
        } catch (OrderNotCancelException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelOrderBySchedule() {

    }

    @Test
    public void payOrder() {
        String userID = "151250032@smail.nju.edu.cn";
        OrderID orderID = new OrderID();
        orderID.setTime(LocalDateTime.of(2018, 6, 28, 16, 42, 10));
        orderID.setEmail(userID);
        try {
            orderService.payOrder(orderID);

        } catch (OrderNotPaymentException e) {
            e.printStackTrace();
        } catch (UserNotExistException e) {
            e.printStackTrace();
        } catch (WrongPwdException e) {
            e.printStackTrace();
        } catch (AlipayBalanceNotAdequateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unsubscribe() {
        String userID = "151250032@smail.nju.edu.cn";
        OrderID orderID = new OrderID();
        orderID.setTime(LocalDateTime.of(2018, 6, 28, 16, 42, 10));
        orderID.setEmail(userID);
        try {
            orderService.unsubscribe(orderID);
        } catch (OrderNotRefundableException e) {
            e.printStackTrace();
        } catch (UserNotExistException e) {
            e.printStackTrace();
        } catch (WrongPwdException e) {
            e.printStackTrace();
        } catch (AlipayBalanceNotAdequateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getOrderByState() {
    }

    @Test
    public void testGet() {
        String userID = "151250032@smail.nju.edu.cn";
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = TimeHelper.getLocalDateTime(Long.parseLong("1530201526000"));
        LocalDateTime formate = TimeHelper.standardTime(localDateTime);
        orderID.setTime(localDateTime);
        orderID.setEmail(userID);
        Order order = orderService.checkOrderDetail(orderID);
        System.out.println(order.getTickets().size());
    }

    @Test
    public void testGet1() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(15);
        programID.setStartTime(LocalDateTime.of(2017, 6, 14, 0, 0, 0));
        Program program = programRepository.findByProgramID(programID);
        System.out.println(program.getName());
        System.out.println(program.getOrders().size());
    }
}