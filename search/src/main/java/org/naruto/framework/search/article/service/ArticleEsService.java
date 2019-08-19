package org.naruto.framework.search.article.service;

import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.search.article.domain.EsArticle;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleEsService {
    public Page<ArticleVo> search(Map map);

    EsArticle save(EsArticle esArticle);

    void delete(String id);
}
