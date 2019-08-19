package org.naruto.framework.common.catalog.controller;


import org.naruto.framework.common.catalog.domain.Catalog;
import org.naruto.framework.common.catalog.service.CatalogService;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @RequestMapping(value = "/v1/catalogs/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    public ResponseEntity<ResultEntity> add(@Validated @RequestBody Catalog catalog){

        return ResponseEntity.ok(ResultEntity.ok(catalogService.save(catalog)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/catalogs/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {


        Page page = catalogService.queryPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }
}
