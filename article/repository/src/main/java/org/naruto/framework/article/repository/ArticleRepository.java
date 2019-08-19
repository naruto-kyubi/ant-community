package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends CustomRepository<Article,String> {

    @Query(value="select distinct ar from Article ar,Follow fw, UserTag uTag where (ar.owner=fw.followUser and fw.user.id=?1) or (uTag.tag member of ar.tags and uTag.userId=?1) ",
            countQuery="select count(distinct ar) from Article ar,Follow fw, UserTag uTag where (ar.owner=fw.followUser and fw.user.id=?1) or (uTag.tag member of ar.tags and uTag.userId=?1)")
    Page<Article> queryArticlesByFollows(String userId, Pageable pageable);

    Article findArticleByPublishedVersion(String publishedVersion);

}