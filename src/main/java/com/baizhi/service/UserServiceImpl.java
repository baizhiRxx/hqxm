package com.baizhi.service;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.MapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public Map showUserByTime() {
        HashMap hashMap = new HashMap();
        ArrayList manList = new ArrayList();
        manList.add(userDao.queryUserNearOneDay("男"));
        manList.add(userDao.queryUserNearSevenDay("男"));
        manList.add(userDao.queryUserNearOneMonth("男"));
        manList.add(userDao.queryUserNearOneyear("男"));
        hashMap.put("man",manList);
        ArrayList womenList = new ArrayList();
        womenList.add(userDao.queryUserNearOneDay("女"));
        womenList.add(userDao.queryUserNearSevenDay("女"));
        womenList.add(userDao.queryUserNearOneMonth("女"));
        womenList.add(userDao.queryUserNearOneyear("女"));
        hashMap.put("women",womenList);
        return hashMap;
    }

    @Override
    public Map<String,List<MapVo>> showUserByAddress() {
        HashMap<String,List<MapVo>> hashMap = new HashMap<String,List<MapVo>>();
        hashMap.put("man",userDao.queryUserByAddress("男"));
        hashMap.put("women",userDao.queryUserByAddress("女"));
        return hashMap;
    }
}
