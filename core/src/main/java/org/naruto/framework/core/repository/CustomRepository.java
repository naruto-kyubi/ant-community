package org.naruto.framework.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Map;

@NoRepositoryBean
public interface CustomRepository<T,ID
        extends Serializable>
        extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {
    Page queryPageByCondition(@Param("condition") Map condition);

    void increateCount(String id, String column, Long step);

}