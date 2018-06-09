package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.repository.VenueRepository;
import cn.edu.nju.charlesfeng.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<String> getAllCity() {
        return venueRepository.getAllCity();
    }
}
