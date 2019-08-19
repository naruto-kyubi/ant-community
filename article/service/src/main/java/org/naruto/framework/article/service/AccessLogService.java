package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Map;

public interface AccessLogService {

    AccessLog save(AccessLog accessLog);

    Page<Map> queryMoreAccessLogs(Date fromDate, Pageable pageable);
}
