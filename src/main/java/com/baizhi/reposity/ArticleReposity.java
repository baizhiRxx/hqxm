package com.baizhi.reposity;

import com.baizhi.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ArticleReposity extends ElasticsearchCrudRepository<Article,String> {
}
