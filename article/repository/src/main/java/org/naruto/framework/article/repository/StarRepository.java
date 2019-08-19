package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Star;
import org.naruto.framework.core.repository.CustomRepository;

public interface StarRepository extends CustomRepository<Star,String> {

    public Star queryStarByUserIdAndArticleId(String userId, String articleId);

    public void deleteByUserIdAndArticleId(String userId, String articleId);

}