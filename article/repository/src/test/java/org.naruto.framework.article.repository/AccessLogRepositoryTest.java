package org.naruto.framework.article.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.article.domain.ArticleStatus;
import org.naruto.framework.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = FrameworkApplication.class)
public class AccessLogRepositoryTest {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Test
    public void testQueryMoreAccessLogs(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        c.add(Calendar.DATE, - 7);
        Date beforeDate = c.getTime();

        Map map = new HashMap();
        map.put("sorter","viewCount_desc,updatedAt_desc");
//        map.put("status_equal", ArticleStatus.PUBLISH.toString());
//        map.put("updatedAt_between",format.format(beforeDate) + "," + format.format(currentDate));

        Pageable pagable = PageUtils.createPageable(map);
        Page page = accessLogRepository.queryMoreAccessLogs(beforeDate,pagable);
        System.out.println(page.getTotalElements());
    }
}
