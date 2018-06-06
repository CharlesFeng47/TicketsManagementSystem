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

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void testAdd() {
//        Venue venue = new Venue();
//        Address address = new Address();
//        address.setCity("南京");
//        address.setDistrict("鼓楼");
//        address.setStreet("汉口路");
//        address.setNumber("100号");
//        address.setComment("(南京大学附近)");
//        venue.setVenueName("清高体育馆");
//        venue.setAlipayId("1234567890");
//        venue.setAddress(address);
//        Venue venue1 = venueRepository.save(venue);
//        Assert.assertEquals(1, venue1.getVenueID());
        Map<String, String> map = readAddress();
        List<String> streets = Arrays.asList("路", "街", "道", "巷", "滩");
        Iterable<Venue> venues = new HashSet<>();
        for (String key : map.keySet()) {
            String names[] = key.split("\\s");
            Venue venue = new Venue();
            venue.setVenueName(names[1]);
            Address address = new Address();
            address.setCity(names[0]);
            String path = map.get(key);

            if (path.contains("市")) {
                int index_ci = path.indexOf("市");
                path = path.substring(index_ci + 1);
            }

            String distract = null;
            if (path.contains("区")) {
                int index_dis = path.indexOf("区");
                address.setDistrict(path.substring(0, index_dis));
                distract = path.substring(index_dis + 1);
            } else {
                address.setDistrict("");
                distract = path;
            }

            boolean flag = true;
            for (String name : streets) {
                if (distract.contains(name)) {
                    flag = false;
                    int index = distract.indexOf(name);
                    address.setStreet(distract.substring(0, index) + name);
                    String num = distract.substring(index + 1);
                    if (num.contains("号")) {
                        int index_num = num.indexOf("号");
                        address.setNumber(num.substring(0, index_num));
                        address.setComment(num.substring(index_num + 1));
                    } else {
                        address.setNumber("");
                        address.setComment(num);
                    }
                    break;
                }
            }
            if (flag) {
                address.setStreet("");
                if (distract.contains("号")) {
                    int index_num = distract.indexOf("号");
                    address.setNumber(distract.substring(0, index_num));
                    address.setComment(distract.substring(index_num + 1));
                } else {
                    address.setNumber("");
                    address.setComment(distract);
                }
            }
            venue.setAddress(address);
            ((HashSet<Venue>) venues).add(venue);
        }
        venueRepository.saveAll(venues);
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
        if (programSeat == null) {
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

    private Map<String, String> readAddress() {
        File file = new File("F:\\crawler\\address.txt");
        Map<String, String> result = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String temp[] = line.split(";");
                result.put(temp[0], temp[1]);
            }
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}