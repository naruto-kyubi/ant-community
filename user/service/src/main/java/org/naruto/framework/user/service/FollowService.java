package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.Follow;
import org.springframework.data.domain.Page;

import java.util.Map;


public interface FollowService {

    Follow queryFollowByUserIdAndFollowUserId(String userId, String followUserId);

    Follow query(String userId, String followUserId);

    Follow save(Follow follow);

    void delete(String userId, String followUserId);

    Page queryFollowUsers(Map map);

    Page queryFans(Map map);
}
