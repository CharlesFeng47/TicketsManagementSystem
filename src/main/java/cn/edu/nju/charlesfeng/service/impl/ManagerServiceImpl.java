package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.UnexaminedSpot;
import cn.edu.nju.charlesfeng.service.ManagerService;

import java.util.Set;

public class ManagerServiceImpl implements ManagerService {


    @Override
    public Set<UnexaminedSpot> getAllUnexaminedSpotApplications() {
        return null;
    }

    @Override
    public boolean examine() {
        return false;
    }

    @Override
    public boolean settle() {
        return false;
    }
}
