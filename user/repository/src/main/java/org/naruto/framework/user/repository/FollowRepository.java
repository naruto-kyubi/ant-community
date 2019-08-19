package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface FollowRepository extends CustomRepository<Follow,String> {

    Follow queryFollowByUserIdAndFollowUserId(String userId, String followUserId);

    void deleteByUserIdAndFollowUserId(String userId, String followUserId);

    List<Follow> queryFollowsByUserId(String userId);

    List<Follow> queryFollowsByFollowUserId(String followUserId);

//    @Query(value = "SELECT fr.follow_user_id AS id,users.nickname AS nickname,users.avatar AS avatar,users.profile AS profile,fr.c_mutual AS mutual,fr.updated_at,fr.c_user_id FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.follow_user_id=f2.c_follow_user_id)) fr,users WHERE fr.follow_user_id=users.id order by ?#{#pageable}",
//            countQuery="SELECT count(*) FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.follow_user_id=f2.c_follow_user_id)) fr,users WHERE fr.follow_user_id=users.id",
//            nativeQuery = true)
    @Query(value = "SELECT fr.follow_user_id AS id,users.nickname AS nickname,users.avatar AS avatar,users.profile AS profile,fr.c_mutual AS mutual,fr.updated_at,fr.c_user_id FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.follow_user_id=f2.c_follow_user_id)) fr,users WHERE fr.follow_user_id=users.id",
            countQuery="SELECT count(*) FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.follow_user_id=f2.c_follow_user_id)) fr,users WHERE fr.follow_user_id=users.id",
            nativeQuery = true)
    Page<Map> findAll(@Param("userId") String userId, @Param("currentUserId") String currentUserId, Pageable pageable);

    @Query(value = "SELECT fr.user_id AS id,users.nickname AS nickname,users.avatar AS avatar,users.profile AS profile,fr.c_mutual AS mutual,fr.updated_at,fr.c_user_id FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE follow_user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.user_id=f2.c_follow_user_id)) fr,users WHERE fr.user_id=users.id",
            countQuery="SELECT count(*) FROM (SELECT user_id,follow_user_id,updated_at,c_user_id,c_mutual FROM ((SELECT user_id,follow_user_id,updated_at FROM follows WHERE follow_user_id=?1) AS f1 LEFT JOIN (SELECT user_id AS c_user_id,follow_user_id AS c_follow_user_id,mutual AS c_mutual FROM follows WHERE user_id=?2) AS f2 ON f1.user_id=f2.c_follow_user_id)) fr,users WHERE fr.user_id=users.id",
            nativeQuery = true)
    Page<Map> queryFans(@Param("followUserId") String followUserId, @Param("currentUserId") String currentUserId, Pageable pageable);
}
