package org.naruto.framework.user.service;

import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.Mutual;
import org.naruto.framework.user.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Service
public class FollowServiceImp implements FollowService {



    @Autowired
    private FollowRepository followRepository;


    @Override
    public Follow queryFollowByUserIdAndFollowUserId(String userId, String followUserId) {
        return followRepository.queryFollowByUserIdAndFollowUserId(userId,followUserId);
    }

    public Follow query(String userId, String followUserId){
       return followRepository.queryFollowByUserIdAndFollowUserId(userId,followUserId);
    }

    public Follow save(Follow follow){

        Follow f = followRepository.queryFollowByUserIdAndFollowUserId(follow.getFollowUser().getId(),follow.getUser().getId());
        if(null==f){
            //only one follow；
            follow.setMutual(Mutual.FOLLOW.getValue());
        }else{
            //both
            f.setMutual(Mutual.BOTH.getValue());
            followRepository.save(f);
            follow.setMutual(Mutual.BOTH.getValue());
        }
        return followRepository.save(follow);
    }

    public void delete(String userId,String followUserId){
        Follow f = followRepository.queryFollowByUserIdAndFollowUserId(followUserId,userId);
        if(null!=f){
            f.setMutual(Mutual.FOLLOW.getValue());
            followRepository.save(f);
        }
        followRepository.deleteByUserIdAndFollowUserId(userId,followUserId);
    }

    @Override
    public Page queryFollowUsers(Map map) {
        String userId = (String) map.get("userId");
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return followRepository.findAll(userId,currentUserId,pageable);
    }

    @Override
    public Page queryFans(Map map) {

        String followUserId = (String) map.get("followUserId");
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return followRepository.queryFans(followUserId,currentUserId,pageable);
    }
}
