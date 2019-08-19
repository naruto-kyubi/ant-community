package org.naruto.framework.common.geographic.repository;

import org.naruto.framework.common.geographic.domain.City;
import org.naruto.framework.core.repository.CustomRepository;

import java.util.List;

public interface CityRepository extends CustomRepository<City,String> {

    List<City> queryCitiesByProvinceIdOrderBySeqAsc(String provinceId);
}
