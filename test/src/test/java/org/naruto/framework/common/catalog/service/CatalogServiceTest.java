package org.naruto.framework.common.catalog.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplicationTest;
import org.naruto.framework.common.catalog.domain.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplicationTest.class)
public class CatalogServiceTest {

    @Autowired
    private CatalogService catalogService;

    @Test
    public void testQueryPage(){
        Map map = new HashMap();
        Page<Catalog> page = catalogService.queryPage(map);
        System.out.println("getTotalElements=" + page.getTotalElements());
    }
}
