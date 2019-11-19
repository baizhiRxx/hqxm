package com.baizhi.service;

import com.baizhi.entity.Banner;

import java.util.List;
import java.util.Map;

public interface BannerService {
    Map queryBanners(Integer page, Integer rows);
    String addBanner(Banner banner);
    void updateBanner(Banner banner);
    void deleteBanners(String[] ids);
}
