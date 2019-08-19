package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeServiceImpl implements LikeService{
    @Autowired
    private LikeRepository likeRepository;

    public Like queryLikeByUserIdAndTypeAndTargetId(String userId,String type,String targetId){

        return likeRepository.queryLikeByUserIdAndTypeAndTargetId(userId,type,targetId);
    }

    public Like saveLike(Like like){
        return likeRepository.save(like);
    }

    public void deleteLike(String userId,String type,String targetId){
        likeRepository.deleteLikeByUserIdAndTypeAndTargetId(userId,type,targetId);
    }

}
