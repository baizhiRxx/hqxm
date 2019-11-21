package com.baizhi.service;

import com.baizhi.dao.ArticleDao;
import com.baizhi.entity.Article;
import com.baizhi.reposity.ArticleReposity;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleReposity articleReposity;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
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
        articleReposity.save(article);
    }

    @Override
    public void updateArticle(Article article) {
        articleDao.updateByPrimaryKeySelective(article);
        articleReposity.save(article);
    }

    @Override
    public void deleteArticles(String[] ids) {
        Example example = new Example(Article.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        articleDao.deleteByExample(example);
        for (String id : ids) {
            articleReposity.deleteById(id);
        }
    }

    @Override
    public List<Article> showArticleByEs(String str) {
        HighlightBuilder.Field field = new HighlightBuilder.Field("*")
                .preTags("<span style='color:red';weight-font:bold>")
                .postTags("</span>");

        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("create_date").order(SortOrder.ASC);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(str).field("content").field("title"))
                .withFilter(QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(str).field("content").field("title")))
                .withSort(fieldSortBuilder)
                .withHighlightFields(field)
                .build();

        AggregatedPage<Article> articles = elasticsearchTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHit[] hits = searchResponse.getHits().getHits();
                ArrayList<Article> articles = new ArrayList<>();
                for (SearchHit hit : hits) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    Article article = new Article();
                    article.setStatus(sourceAsMap.get("status").toString());
                    article.setContent(sourceAsMap.get("content").toString());
                    article.setTitle(sourceAsMap.get("title").toString());
                    article.setGuru_id(sourceAsMap.get("guru_id").toString());
                    article.setId(sourceAsMap.get("id").toString());
                    article.setPublish_date(new Date(Long.valueOf(sourceAsMap.get("publish_date").toString())));
                    article.setCreate_date(new Date(Long.valueOf(sourceAsMap.get("create_date").toString())));

                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields.get("title") != null) {
                        article.setTitle(highlightFields.get("title").getFragments()[0].toString());
                    }
                    if (highlightFields.get("content") != null) {
                        article.setContent(highlightFields.get("content").getFragments()[0].toString());
                    }
                    articles.add(article);
                }
                return (AggregatedPage<T>) new AggregatedPageImpl<Article>(articles);
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });

        return articles.getContent();
    }
}
