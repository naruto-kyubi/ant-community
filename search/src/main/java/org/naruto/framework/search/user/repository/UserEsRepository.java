package org.naruto.framework.search.user.repository;

import org.naruto.framework.search.user.domain.EsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserEsRepository extends ElasticsearchRepository<EsUser,String> {

}
