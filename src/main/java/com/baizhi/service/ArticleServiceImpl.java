package com.baizhi.service;

import com.baizhi.dao.ArticleDao;
import com.baizhi.entity.Article;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Override
    public Map showAllArticles(Integer page, Integer rows) {
        //jqGrid 需要page当前页数  total总页数  rows数据  records总条数
        HashMap hashMap = new HashMap();
        Integer records = articleDao.selectCount(new Article());
        List<Article> articles = articleDao.selectByRowBounds(new Article(), new RowBounds((page - 1) * rows, rows));
        Integer total = records%rows==0?records/rows:records/rows+1;
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",articles);
        hashMap.put("records",records);
        return hashMap;
    }

    @Override
    public void AddArticle(Article article) {
        article.setId(UUID.randomUUID().toString());
        article.setCreate_date(new Date());
        article.setPublish_date(new Date());
        articleDao.insertSelective(article);
    }

    @Override
    public void updateArticle(Article article) {
        articleDao.updateByPrimaryKeySelective(article);
    }

    @Override
    public void deleteArticles(String[] ids) {
        Example example = new Example(Article.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        articleDao.deleteByExample(example);
    }
}
