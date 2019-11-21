package com.baizhi.service;

import com.baizhi.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    Map showAllArticles(Integer page,Integer rows);
    void AddArticle(Article article);
    void updateArticle(Article article);
    void deleteArticles(String[] ids);
    List<Article> showArticleByEs(String str);
}
