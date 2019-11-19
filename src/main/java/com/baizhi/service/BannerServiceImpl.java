package com.baizhi.service;

import com.baizhi.dao.BannerDao;
import com.baizhi.entity.Banner;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerDao bannerDao;
    @Override
    public Map queryBanners(Integer page, Integer rows) {
        //jqGrid 需要page当前页数  total总页数  rows数据  records总条数
        HashMap hashMap = new HashMap();
        List<Banner> banners = bannerDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));
        int records = bannerDao.selectCount(null);
        int total = records%rows==0?records/rows:records/rows+1;
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",banners);
        hashMap.put("records",records);
        return hashMap;
    }
    public String addBanner(Banner banner){
        String id = UUID.randomUUID().toString();
        banner.setId(id);
        banner.setCreate_date(new Date());
        bannerDao.insert(banner);
        return id;
    }
    public void updateBanner(Banner banner){
        bannerDao.updateByPrimaryKeySelective(banner);
    }
    public void deleteBanners(String[] ids){
        Example example = new Example(Banner.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        bannerDao.deleteByExample(example);
    }
}
