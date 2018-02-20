package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.UnexaminedSpotApplication;
import cn.edu.nju.charlesfeng.service.ManagerService;

import java.util.Set;

public class ManagerServiceImpl implements ManagerService {


    @Override
    public Set<UnexaminedSpotApplication> getAllUnexaminedSpotApplications() {
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
