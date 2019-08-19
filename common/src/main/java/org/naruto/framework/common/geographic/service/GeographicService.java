package org.naruto.framework.common.geographic.service;

import org.naruto.framework.common.geographic.domain.City;
import org.naruto.framework.common.geographic.domain.Province;
import org.naruto.framework.common.geographic.repository.CityRepository;
import org.naruto.framework.common.geographic.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeographicService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CityRepository cityRepository;

    public List<Province> queryProvices(){

        return provinceRepository.queryProvincesByOrderBySeqAsc();
    }

    public List<City> queryCitysbyProvinceId(String provinceId){

        return cityRepository.queryCitiesByProvinceIdOrderBySeqAsc(provinceId);
    }
}
