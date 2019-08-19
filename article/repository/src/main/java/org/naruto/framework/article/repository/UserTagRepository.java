package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.article.domain.UserTag;
import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface UserTagRepository extends CustomRepository<UserTag,String> {

    @Query(value="select t.*,ut.userId from (select u1.*,u2.user_id as userId from ((select * from user_tags where user_id=?1) u1 left join (select * from user_tags where user_id=?2) u2 on u1.tag_id=u2.tag_id)) ut,tags t where ut.tag_id=t.id",
            countQuery="select count(*) from (select u1.*,u2.user_id as userId from ((select * from user_tags where user_id=?1) u1 left join (select * from user_tags where user_id=?2) u2 on u1.tag_id=u2.tag_id)) ut,tags t where ut.tag_id=t.id"
            ,nativeQuery =true
    )
    Page<Map> queryUserTags(String userId, String currentUserId, Pageable pageable);

    @Query(value="select t from Tag t ,UserTag u where t=u.tag and u.userId=?1",
            countQuery = "select count(t) from Tag t ,UserTag u where t=u.tag and u.userId=?1")
    Page<Tag> queryUserTags(String userId, Pageable pageable);

    @Query(value="select t.*,ut.userId from tags t left join (select user_id as userId,tag_id from user_tags where user_id=?1) ut on t.id=ut.tag_id",
            countQuery = "select count(*) from tags t left join (select user_id,tag_id from user_tags where user_id=?1) ut on t.id=ut.tag_id",
            nativeQuery = true
    )

    Page<Map> queryTags(String userId, Pageable pageable);

    void deleteUserTagsByUserIdAndTagId(String userId, String tagId);
}
