package com.baizhi.service;

import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminDao adminDao;
    @Override
    public Map login(Admin admin) {
        HashMap hashMap = new HashMap();
        Admin data = adminDao.selectOne(admin);
        if (data==null){
            hashMap.put("status","error");
        }else {
            hashMap.put("status","success");
            hashMap.put("admin",data);
        }
        return hashMap;
    }
}
