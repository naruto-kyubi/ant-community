package org.naruto.framework.common.catalog.service;

import org.naruto.framework.common.catalog.domain.Catalog;
import org.naruto.framework.common.catalog.repository.CatalogRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public Catalog save(Catalog catalog){
        if(catalog == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        return catalogRepository.save(catalog);
    }

    public Page<Catalog> queryPage(Map map) {
        return catalogRepository.queryPageByCondition(map);
    }
}
