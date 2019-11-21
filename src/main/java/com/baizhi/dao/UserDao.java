package com.baizhi.dao;

import com.baizhi.entity.MapVo;
import com.baizhi.entity.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface UserDao extends Mapper<User> {
    Integer queryUserNearOneDay(String gender);
    Integer queryUserNearSevenDay(String gender);
    Integer queryUserNearOneMonth(String gender);
    Integer queryUserNearOneyear(String gender);
    List<MapVo> queryUserByAddress(String gender);
    User queryUserRolePermission(String phoneNumber);
}
