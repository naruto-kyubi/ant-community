package org.naruto.framework.article.repository;

import org.naruto.framework.article.domain.AccessLog;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Map;

public interface AccessLogRepository extends CustomRepository<AccessLog,String> {

    @Query(value="select count(article) as accessCount,al.article as article from AccessLog al where al.createdAt>?1 group by al.article ",
            countQuery = "select count(al) from AccessLog al where al.createdAt>?1 group by al.article",
            nativeQuery = false
    )
    Page<Map> queryMoreAccessLogs(Date fromDate, Pageable pageable);
}
