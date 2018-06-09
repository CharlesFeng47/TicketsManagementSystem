package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.service.VenueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueServiceImplTest {

    @Autowired
    private VenueService venueService;


    @Test
    public void getAllCity() {
        List<String> cities = venueService.getAllCity();
        System.out.println(cities);

    }
}