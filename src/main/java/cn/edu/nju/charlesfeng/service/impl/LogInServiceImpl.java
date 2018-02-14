package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.SpotInfo;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import org.springframework.stereotype.Service;

@Service
public class LogInServiceImpl implements LogInService {

    @Override
    public Member registerMember(String id, String pwd) {
        return null;
    }

    @Override
    public Spot registerSpot(String pwd, SpotInfo spotInfo) {
        return null;
    }

    @Override
    public User logIn(String id, String pwd) {
        return null;
    }
}
