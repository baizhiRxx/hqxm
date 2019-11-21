package com.baizhi.controller;

import com.baizhi.dao.BannerDao;
import com.baizhi.entity.Banner;
import com.baizhi.service.BannerService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private BannerDao bannerDao;
    @RequestMapping("showBanners")
    public Map showBanners(Integer page,Integer rows){
        HashMap hashMap = new HashMap();
        Map map = bannerService.queryBanners(page, rows);
        return map;
    }
    @RequestMapping("editBanner")
    public Map editBanner(Banner banner,String oper,String[] id){
        HashMap hashMap = new HashMap();
        if (oper.equals("add")){
            String bannerId = bannerService.addBanner(banner);
            hashMap.put("bannerId",bannerId);
            hashMap.put("msg","添加成功");
        }else if (oper.equals("edit")){
            bannerService.updateBanner(banner);
            hashMap.put("msg","变更成功");
        }else if (oper.equals("del")){
            bannerService.deleteBanners(id);
            hashMap.put("msg","删除成功");
        }
        return hashMap;
    }
    @RequestMapping("uploadBanner")
    public void uploadBanner(MultipartFile url, String bannerId, HttpSession session, HttpServletRequest request){
        String realPath = session.getServletContext().getRealPath("/img");
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String originalFilename = url.getOriginalFilename();
        String name = new Date().getTime() + "_" + originalFilename;
        try {
            url.transferTo(new File(realPath, name));
            Banner banner = new Banner();
            banner.setId(bannerId);
            banner.setUrl(name);
            bannerDao.updateByPrimaryKeySelective(banner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("outBanner")
    public void outBanner(HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet bannerSheet = workbook.createSheet("轮播图信息");
        // 设置表头
        HSSFRow rowTitle = bannerSheet.createRow(0);
        String[] title = {"编号", "标题", "状态", "描述", "创建时间", "图片"};
        for (int i = 0; i < title.length; i++) {
            bannerSheet.setColumnWidth(0,10000);
            bannerSheet.setColumnWidth(1,5000);
            bannerSheet.setColumnWidth(2,5000);
            bannerSheet.setColumnWidth(3,5000);
            bannerSheet.setColumnWidth(4,5000);
            bannerSheet.setColumnWidth(5,10000);
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(title[i]);
        }
        // 处理时间样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd"));
        // 导出数据
        List<Banner> banners = bannerDao.selectAll();
        for (int i = 1; i <= banners.size(); i++) {
            Banner banner = banners.get(i - 1);
            HSSFRow row = bannerSheet.createRow(i);
            row.createCell(0).setCellValue(banner.getId());
            row.createCell(1).setCellValue(banner.getTitle());
            row.createCell(2).setCellValue(banner.getStatus().equals("1")?"展示":"不展示");
            row.createCell(3).setCellValue(banner.getDescription());
            HSSFCell cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(banner.getCreate_date());
            row.createCell(5).setCellValue(banner.getUrl());
        }
        response.setHeader("content-disposition", "attachment;fileName=bannerInfo.xls");
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("outBannerModel")
    public void outBannerModel(HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet bannerSheet = workbook.createSheet("轮播图信息");
        // 设置表头
        HSSFRow rowTitle = bannerSheet.createRow(0);
        String[] title = {"编号", "标题", "状态", "描述", "创建时间", "图片"};
        for (int i = 0; i < title.length; i++) {
            bannerSheet.setColumnWidth(0,10000);
            bannerSheet.setColumnWidth(1,5000);
            bannerSheet.setColumnWidth(2,5000);
            bannerSheet.setColumnWidth(3,5000);
            bannerSheet.setColumnWidth(4,5000);
            bannerSheet.setColumnWidth(5,10000);
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(title[i]);
        }
        response.setHeader("content-disposition", "attachment;fileName=bannerInfo.xls");
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("importBanner")
    public void importBanner(MultipartFile upload){
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(upload.getInputStream());
            HSSFSheet bannerTable = workbook.getSheet("轮播图信息");
            for (int i = 1; i <= bannerTable.getLastRowNum(); i++) {
                HSSFRow row = bannerTable.getRow(i);
                Banner banner = new Banner();
                banner.setId(UUID.randomUUID().toString());
                banner.setTitle(row.getCell(1).getStringCellValue());
                banner.setStatus(row.getCell(2).getStringCellValue());
                banner.setDescription(row.getCell(3).getStringCellValue());
                banner.setCreate_date(row.getCell(4).getDateCellValue());
                banner.setUrl(row.getCell(5).getStringCellValue());
                bannerDao.insertSelective(banner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

