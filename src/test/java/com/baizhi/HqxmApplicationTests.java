package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.baizhi.controller.UserController;
import com.baizhi.dao.UserDao;
import com.baizhi.entity.MapVo;
import com.baizhi.entity.StudentEntity;
import com.baizhi.entity.User;
import io.goeasy.GoEasy;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class HqxmApplicationTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserController userController;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void poi_export() {
        List<User> users = userDao.selectAll();
        // 创建Excel工作簿对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet("用户表");
        // 创建标题行
        HSSFRow row = sheet.createRow(0);
        // 创建单元格对象
        String[] title = {"编号","姓名","签名","注册时间"};
        for (int i = 0; i < title.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        short format = dataFormat.getFormat("yyyy-MM-dd");
        cellStyle.setDataFormat(format);
        for (int i = 0; i < users.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            User user = users.get(i);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getName());
            dataRow.createCell(2).setCellValue(user.getSign());
            // 时间格式无法在Excel中正确显示 需要手动设置显示样式
            HSSFCell cell = dataRow.createCell(3);
            cell.setCellValue(user.getRegistTime());
            cell.setCellStyle(cellStyle);
        }
        try {
            workbook.write(new File("C:\\360DownLoad\\user.xls"));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void poi_import(){
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("C:\\360DownLoad\\user.xls")));
            HSSFSheet userTable = workbook.getSheet("用户表");
            for (int i = 1; i <= userTable.getLastRowNum(); i++) {
                HSSFRow row = userTable.getRow(i);
                String id = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String sign = row.getCell(2).getStringCellValue();
                Date rigst_date = row.getCell(3).getDateCellValue();
                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setSign(sign);
                user.setRegistTime(rigst_date);
                System.out.println(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void easy_poi(){
        StudentEntity studentEntity1 = new StudentEntity("1","Rx",2,new Date(),new Date());
        StudentEntity studentEntity2 = new StudentEntity("2","Rx",2,new Date(),new Date());
        StudentEntity studentEntity3 = new StudentEntity("3","Rx",2,new Date(),new Date());
        ArrayList<Object> list = new ArrayList<>();
        list.add(studentEntity1);
        list.add(studentEntity2);
        list.add(studentEntity3);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生","学生"),
                StudentEntity.class, list);
        try {
            workbook.write(new FileOutputStream(new File("C:\\360DownLoad\\user.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void userDao(){
//        Integer i = userDao.queryUserNearOneDay("男");
//        System.out.println(i);
//        Integer n = userDao.queryUserNearOneMonth("");
//        System.out.println(n);
        //int i = userDao.selectCount(new User());
        //System.out.println(i);
        List<MapVo> maps = userDao.queryUserByAddress("男");
        System.out.println(maps);
    }
    @Test
    public void goeasy(){
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io","BC-4909d2f4dea84846b41e97b90dd224a4");
        Map map = userController.showUserByAddress();
        String string = JSONObject.toJSONString(map);
        goEasy.publish("cmfz", string);
    }
    @Test
    public void testRedis(){
        stringRedisTemplate.opsForValue().set("name","Rx");
    }
}
