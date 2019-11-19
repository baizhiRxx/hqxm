package com.baizhi.dao;

import com.baizhi.entity.MapVo;
import com.baizhi.entity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface UserDao extends Mapper<User> {
    @Select("select count(*) from user where gender = #{gender} and date_sub(now(),interval 1 day) <= date(registTime)")
    Integer queryUserNearOneDay(String gender);
    @Select("select count(*) from user where gender = #{gender} and date_sub(now(),interval 7 day) <= date(registTime)")
    Integer queryUserNearSevenDay(String gender);
    @Select("select count(*) from user where gender = #{gender} and date_sub(now(),interval 30 day) <= date(registTime)")
    Integer queryUserNearOneMonth(String gender);
    @Select("select count(*) from user where gender = #{gender} and date_sub(now(),interval 365 day) <= date(registTime)")
    Integer queryUserNearOneyear(String gender);
    @Select("select location as name,count(*) as value from user where gender = #{gender} group by location")
    List<MapVo> queryUserByAddress(String gender);
}
