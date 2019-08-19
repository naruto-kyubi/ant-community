package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Like;

public interface LikeService {
    //like,zhan;

    Like queryLikeByUserIdAndTypeAndTargetId(String userId, String type, String targetId);

    Like saveLike(Like like);

    void deleteLike(String userId, String type, String targetId);
}
