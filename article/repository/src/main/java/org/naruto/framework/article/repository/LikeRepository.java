package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Like;
import org.naruto.framework.core.repository.CustomRepository;

public interface LikeRepository extends CustomRepository<Like,String> {
    
    public Like queryLikeByUserIdAndTypeAndTargetId(String userId, String type, String targetId);

    public void deleteLikeByUserIdAndTypeAndTargetId(String userId, String type, String targetId);

}
