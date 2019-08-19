package org.naruto.framework.search.search.service;

import org.naruto.framework.search.article.domain.EsArticle;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SearchEsService {
    Page<Map> searchMutiIndices(Map map);

    Page<EsArticle> searchLikeThis(Map map);
}
