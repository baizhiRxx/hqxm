package com.baizhi.controller;

import com.baizhi.entity.Article;
import com.baizhi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @RequestMapping("showAllArticles")
    public Map showAllArticles(Integer page,Integer rows){
        Map map = articleService.showAllArticles(page, rows);
        return map;
    }
    @RequestMapping("uploadImg")
    public Map uploadImg(MultipartFile imgFile, HttpSession session){
        HashMap hashMap = new HashMap();
        String realPath = session.getServletContext().getRealPath("/img");
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String imgName = new Date().getTime()+"_"+imgFile.getOriginalFilename();
        try {
            imgFile.transferTo(new File(realPath,imgName));
            hashMap.put("error",0);
            hashMap.put("url","../img/"+imgName);
        } catch (IOException e) {
            hashMap.put("error",1);
            hashMap.put("url","上传失败");
            e.printStackTrace();
        }finally {
            return hashMap;
        }
    }
    @RequestMapping("addArticle")
    public void addArticle(Article article){
        articleService.AddArticle(article);
    }
    @RequestMapping("updateArticle")
    public void updateArticle(Article article){
        articleService.updateArticle(article);
    }
    @RequestMapping("editArticle")
    public void editArticle(String[] id,String oper,Article article){
        if (oper.equals("del")){
            articleService.deleteArticles(id);
        }
    }
}
