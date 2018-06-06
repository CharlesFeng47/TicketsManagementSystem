package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.SeatID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeatRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void testAdd() {
        Venue venue = venueRepository.getOne(1);
        Set<Seat> seats = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Seat seat = new Seat();
                SeatID seatID = new SeatID();
                seatID.setVenueID(venue.getVenueID());
                seatID.setCol(j);
                seatID.setRow(i);
                seat.setSeatID(seatID);
                seat.setType("A区");
                seat.setVenue(venue);
                seatRepository.save(seat);
                seats.add(seat);
            }
        }
        venue.setSeats(seats);
        venueRepository.save(venue);
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


}