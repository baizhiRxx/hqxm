package com.baizhi;

import com.baizhi.dao.ArticleDao;
import com.baizhi.entity.Article;
import com.baizhi.reposity.ArticleReposity;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestEs {
    @Autowired
    private ArticleReposity articleReposity;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void test01(){
        List<Article> articles = articleDao.selectAll();
        articleReposity.saveAll(articles);
    }
    @Test
    public void test02(){
        Iterable<Article> all = articleReposity.findAll();
        all.forEach(article -> System.out.println(article));
    }
    @Test
    public void test03(){
        String str = "不展示";
        // 创建高亮字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("*")
                .preTags("<span sytle='color:red';font:bold>")
                .postTags("</span>");
        // 排序
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("create_date").order(SortOrder.ASC);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(str).field("title").field("content").field("status"))
                .withFilter(QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(str).field("title").field("content").field("status")))
                .withSort(fieldSortBuilder)
                .withHighlightFields(field).build();

        AggregatedPage<Article> articles = elasticsearchTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHit[] hits = searchResponse.getHits().getHits();
                ArrayList<Article> list = new ArrayList<>();
                for (SearchHit hit : hits) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    Article article = new Article();
                    article.setId(sourceAsMap.get("id").toString());
                    article.setTitle(sourceAsMap.get("title").toString());
                    article.setPublish_date(new Date(Long.valueOf(sourceAsMap.get("publish_date").toString())));
                    article.setCreate_date(new Date(Long.valueOf(sourceAsMap.get("create_date").toString())));
                    article.setContent(sourceAsMap.get("content").toString());
                    //article.setGuru_id(sourceAsMap.get("guru_id").toString());
                    article.setStatus(sourceAsMap.get("status").toString());

                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields.get("title") != null) {
                        article.setTitle(highlightFields.get("title").getFragments()[0].toString());
                    }
                    if (highlightFields.get("content") != null) {
                        article.setContent(highlightFields.get("content").getFragments()[0].toString());
                    }
                    if (highlightFields.get("status") != null) {
                        article.setStatus(highlightFields.get("status").getFragments()[0].toString());
                    }
                    list.add(article);
                }
                return (AggregatedPage<T>) new AggregatedPageImpl<Article>(list);
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                System.out.println("aaa");
                return null;
            }
        });
        System.out.println(articles.getContent());
    }
}
