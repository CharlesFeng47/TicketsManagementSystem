package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.Venue;
import cn.edu.nju.charlesfeng.model.id.SeatID;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

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

    @Test
    public void testAddAll() {
//        Venue venue1 = venueRepository.getOne(143);
//        Venue venue2 = venueRepository.getOne(144);
        Venue venue3 = venueRepository.getOne(145);
//        List<Venue> venues = venueRepository.findAll();
        List<Venue> venues = new ArrayList<>();
        venues.add(venue3);

        for (Venue venue : venues) {
            Hibernate.initialize(venue);
            System.out.println(venue.getVenueName() + "----开始生成座位");
            String seat_XY = randomSeat();
            String xy[] = seat_XY.split(";");
            int col_a = Integer.parseInt(xy[0]);
            int row_a = Integer.parseInt(xy[1]);
            System.out.println("座位：col-" + col_a + "\t" + "row-" + row_a);
            Iterable<Seat> seats = new HashSet<>();
            for (int i = 1; i <= row_a; i++) {
                for (int j = 1; j < col_a; j++) {
                    SeatID seatID = new SeatID();
                    seatID.setCol(j);
                    seatID.setRow(i);
                    seatID.setVenueID(venue.getVenueID());
                    Seat seat = new Seat();
                    seat.setSeatID(seatID);
                    seat.setVenue(venue);
                    seat.setType(createAreaName(j, i, col_a, row_a));
                    ((HashSet<Seat>) seats).add(seat);
                    System.out.println("col:" + j + " row:" + i + " area:" + seat.getType());
                }
            }
            venue.setSeats((Set<Seat>) seats);
            seatRepository.saveAll(seats);
            seatRepository.flush();
            System.out.println("座位添加完成");
            venueRepository.save(venue);
            venueRepository.flush();
            System.out.println("场馆更新完成");
            System.out.println("------------------------------------------------");
        }
    }

//    public static void main(String[] args) {
//        SeatRepositoryTest seatRepositoryTest = new SeatRepositoryTest();
//        System.out.println(seatRepositoryTest.randomSeat());
//    }

    private String createAreaName(int col, int row, int col_a, int row_a) {
        Map<Integer, List<Integer>> col_area = createArea(col_a);
        Map<Integer, List<Integer>> row_area = createArea(row_a);

        char result = 'A';
        for (Integer key : col_area.keySet()) {
            if (col_area.get(key).contains(col)) {
                result = (char) (result + (key - 1));
                break;
            }
        }

        for (Integer key : row_area.keySet()) {
            if (row_area.get(key).contains(row)) {
                result = (char) (result + ((key - 1) * col_area.keySet().size()));
                break;
            }
        }
        return String.valueOf(result) + "区";
    }


    private Map<Integer, List<Integer>> createArea(int all) {
        Map<Integer, List<Integer>> area = new HashMap<>();
        if (all >= 15) {
            area.put(1, new ArrayList<>());
            area.put(2, new ArrayList<>());
            area.put(3, new ArrayList<>());

            int temp = all;
            if (all % 3 != 0) {
                temp = temp - 1;
            }
            int per = temp / 3;
            for (int i = 1; i <= temp; i++) {
                if (i <= per) {
                    area.get(1).add(i);
                    continue;
                }

                if (i <= per * 2) {
                    area.get(2).add(i);
                    continue;
                }

                area.get(3).add(i);
            }

            if (all % 3 != 0) {
                int last_2 = area.get(2).get(area.get(2).size() - 1);
                area.get(2).add(last_2 + 1);

                area.get(3).remove(0);
                int last_3 = area.get(3).get(area.get(3).size() - 1);
                area.get(3).add(last_3 + 1);
            }
        } else {
            area.put(1, new ArrayList<>());
            area.put(2, new ArrayList<>());

            int per = all / 2;
            for (int i = 1; i <= all; i++) {
                if (i <= per) {
                    area.get(1).add(i);
                } else {
                    area.get(2).add(i);
                }
            }
        }
        return area;
    }

    public String randomSeat() {
        Random random = new Random(15);
        int col = (int) (Math.random() * 15 + 7);
        int row = (int) (Math.random() * 15 + 6);
        while (col < row || row < 0) {
            row = random.nextInt() + 6;
        }
        return String.valueOf(col) + ";" + String.valueOf(row);
    }


}