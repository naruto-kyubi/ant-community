package org.naruto.framework.article.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.article.domain.ArticleStatus;
import org.naruto.framework.article.service.AccessLogService;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;

    @ResponseBody
    @RequestMapping(value = "/v1/articles/hot", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryHotList(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        c.add(Calendar.DATE, - 7);
        Date beforeDate = c.getTime();
        format.format(currentDate);
        map.put("sorter","accessCount_desc");

        map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(map);

        Page page = accessLogService.queryMoreAccessLogs(beforeDate,pageable);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

}
