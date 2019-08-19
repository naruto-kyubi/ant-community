package org.naruto.framework.common.geographic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.io.IOUtils;
import org.naruto.framework.common.geographic.domain.City;
import org.naruto.framework.common.geographic.domain.Province;
import org.naruto.framework.common.geographic.repository.CityRepository;
import org.naruto.framework.common.geographic.repository.ProvinceRepository;
import org.naruto.framework.common.geographic.service.GeographicService;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GeographicController {
    @Autowired
    private GeographicService geographicService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CityRepository cityRepository;

    @ResponseBody
    @RequestMapping(value = "/v1/geographic/province", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryProvinces(
            HttpServletRequest request, HttpServletResponse response) {

        List<Province> list = geographicService.queryProvices();
        return ResponseEntity.ok(ResultEntity.ok(list));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/geographic/city/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryCitys(
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response) {

        List<City> list = geographicService.queryCitysbyProvinceId(id);
        return ResponseEntity.ok(ResultEntity.ok(list));
    }
//
//    @ResponseBody
//    @RequestMapping(value = "/v1/geographic/todb", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
//    public ResponseEntity<ResultEntity> toDB(
//            HttpServletRequest request, HttpServletResponse response) throws IOException {
////        InputStream inputStream = new FileInputStream("/Users/liuhy/Documents/naruto/naruto-server/src/main/resources/province.json");
////        String text = IOUtils.toString(inputStream,"utf8");
//////        List<Province> s= JSON.parseArray(text, Province.class);
////        JSONArray array = JSON.parseArray(text);
////        List list = new ArrayList();
////        int i = 0;
////        for (Object o : array) {
////            JSONObject jo = (JSONObject) o;
////            Province p = new Province();
////            p.setId(jo.getString("id"));
////            p.setName(jo.getString("name"));
////            p.setSeq( i++);
////            list.add(p);
////        }
////        provinceRepository.saveAll(list);
//
//        InputStream inputStream = new FileInputStream("/Users/liuhy/Documents/naruto/naruto-server/src/main/resources/city.json");
//        String text = IOUtils.toString(inputStream,"utf8");
//        JSONObject obj = (JSONObject) JSON.parse(text);
//        List list = new ArrayList();
//        for (Map.Entry<String, Object> stringObjectEntry : obj.entrySet()) {
//            String pId = stringObjectEntry.getKey();
//            JSONArray cityArray = (JSONArray) stringObjectEntry.getValue();
//            int i = 1;
//            for (Object o : cityArray) {
//                JSONObject jo  = (JSONObject) o;
//                City c = new City();
//                c.setId(jo.getString("id"));
//                c.setName(jo.getString("name"));
//                c.setProvinceId(pId);
//                c.setSeq(i++);
//                list.add(c);
//            }
//
//        }
//        cityRepository.saveAll(list);
//
//        return ResponseEntity.ok(ResultEntity.ok(null));
//    }

}
