package org.naruto.framework.search.article.repository;

import org.naruto.framework.search.article.domain.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleEsRepository extends ElasticsearchRepository<EsArticle,String> {
    List<EsArticle> findArticlesByTitleLike(String title);
}
