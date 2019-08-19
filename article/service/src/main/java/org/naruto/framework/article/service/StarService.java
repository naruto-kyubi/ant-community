package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Star;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface StarService{

    Page<Star> queryStarByPage(Map map);
    Star queryStarByUserIdAndArticleId(String userId, String articleId);
    Star saveStar(Star star);
    public void deleteStar(String userId, String articleId);
}
