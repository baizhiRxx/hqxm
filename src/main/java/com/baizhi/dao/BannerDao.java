package com.baizhi.dao;

import com.baizhi.cache.MyBatisCache;
import com.baizhi.entity.Banner;
import org.apache.ibatis.annotations.CacheNamespace;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@CacheNamespace(blocking = true,implementation = MyBatisCache.class)
public interface BannerDao extends Mapper<Banner> {
}
