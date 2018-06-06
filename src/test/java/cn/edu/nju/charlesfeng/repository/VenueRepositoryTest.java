package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Address;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void testAdd() {
        Venue venue = new Venue();
        Address address = new Address();
        address.setCity("南京");
        address.setDistrict("鼓楼");
        address.setStreet("汉口路");
        address.setNumber("100号");
        address.setComment("(南京大学附近)");
        venue.setVenueName("清高体育馆");
        venue.setAlipayId("1234567890");
        venue.setAddress(address);
        Venue venue1 = venueRepository.save(venue);
        Assert.assertEquals(1, venue1.getVenueID());
    }

    @Test
    public void testModify() {
        Venue venue = venueRepository.getOne(1);
//        Set<Seat> seats = new HashSet<>();
//        Iterator<Seat> iterator = venue.getSeats().iterator();
//        while(iterator.hasNext()){
//            Seat seat = iterator.next();
//            if(seat.getSeatID().getRow()>5){
//                seat.setType("B区");
//            }
//            seats.add(seat);
//        }
//        venue.setSeats(seats);
        ProgramID programID = new ProgramID();
        programID.setVenueID(1);
        programID.setStartTime(LocalDateTime.of(2018, 7, 12, 18, 0, 0));
        Program program = programRepository.getOne(programID);
        Set<Program> programSeat = venue.getPrograms();
        if(programSeat == null){
            programSeat = new HashSet<>();
        }
        programSeat.add(program);
        venue.setPrograms(programSeat);
        venueRepository.save(venue);
    }

    @Test
    public void testGet() {
        Venue venue = venueRepository.getOne(1);
        Assert.assertEquals(100, venue.getSeats().size());
        System.out.println(venue.getVenueID());
        System.out.println(venue.getVenueName());
        System.out.println(venue.getAlipayId());
        System.out.println(venue.getAddress().getComment());
        System.out.println(venue.getSeats().size());
        System.out.println(venue.getPrograms().size());
        System.out.println("---------------------------");
    }

    @Test
    public void testDelete() {
    }
}