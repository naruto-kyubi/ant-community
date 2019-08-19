package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.AccessLog;
import org.naruto.framework.article.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class AccessLogServiceImpl implements AccessLogService{
    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public AccessLog save(AccessLog accessLog) {

        return accessLogRepository.save(accessLog);
    }

    @Override
    public Page<Map> queryMoreAccessLogs(Date fromDate, Pageable pageable) {
        return accessLogRepository.queryMoreAccessLogs(fromDate,pageable);
    }
}
