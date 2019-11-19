package com.baizhi.service;

import com.baizhi.entity.MapVo;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map showUserByTime();
    Map<String,List<MapVo>> showUserByAddress();
}
