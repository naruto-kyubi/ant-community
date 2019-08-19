package org.naruto.framework.common.geographic.repository;

import org.naruto.framework.common.geographic.domain.Province;
import org.naruto.framework.core.repository.CustomRepository;

import java.util.List;

public interface ProvinceRepository extends CustomRepository<Province,String> {
    List<Province> queryProvincesByOrderBySeqAsc();
}
