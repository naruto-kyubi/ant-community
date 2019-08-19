package org.naruto.framework.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.core.repository.SearchItem;
import org.springframework.data.domain.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class PageUtils {
    public static Map prepareQueryPageMap(Map map) {
        if (null == map) map = new HashMap();

        Integer currentPage = 1;
        if(null!=map.get("currentPage")){
            if(map.get("currentPage").getClass()==String.class){
                currentPage = Integer.valueOf((String)map.get("currentPage"));
            }else{
                currentPage = (Integer) map.get("currentPage");
            }
        }

        Integer pageSize = 10;
        if(null!=map.get("pageSize")){
            if(map.get("pageSize").getClass()==String.class){
                pageSize = Integer.valueOf((String)map.get("pageSize"));
            }else{
                pageSize = (Integer) map.get("pageSize");
            }
        }
        currentPage = currentPage - 1;
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        return map;
    }

    public static Pageable createPageable(Map map) {
        Integer currentPage  = (Integer) map.get("currentPage");
        Integer pageSize  = (Integer) map.get("pageSize");
        String sorter = (String) map.get("sorter");
        //分页信息
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        if (StringUtils.isNotEmpty(sorter)) {
            String[] sorters = sorter.split(",");
            if(null==sorters || sorters.length<0){
                sorters=new String[1];
                sorters[0] = sorter;
            }
            Sort sort = null;
            for (String obj : sorters) {
                int pos = obj.lastIndexOf("_");
                String s = obj.substring(pos+1);
                String column = obj.substring(0,pos);

                Sort.Direction direction ="ascend".equalsIgnoreCase(s)? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = null==sort?new Sort(direction, column) : sort.and(new Sort(direction, column));
            }
            pageable = PageRequest.of(currentPage, pageSize, sort);
        }
        return pageable;
    }

    public static void main(String[] args){
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        c.add(Calendar.DATE, - 7);
        Date beforeDate = c.getTime();
        format.format(currentDate);
        map.put("between",format.format(beforeDate) + "," + format.format(currentDate));
        map =prepareQueryPageMap(map);
        Pageable pageable = createPageable(map);
        System.out.println(pageable.toString());
    }

    public static Map wrapperPagination(Page page){
        Pagination pagination = new Pagination(page.getSize(),page.getNumber()+1,page.getTotalElements());
        Map pageMap  = new HashMap();
        pageMap.put("pagination",pagination);
        return pageMap;
    }

    public static Map clearPaginationArgs(Map map){
        if(map.containsKey("currentPage")) map.remove("currentPage");
        if(map.containsKey("pageSize")) map.remove("pageSize");
        if(map.containsKey("sorter")) map.remove("sorter");
        return map;
    }

    public static Page wrapperVoPage(Page page,Class clazz){
        List list = page.getContent();
        List voList = ObjUtils.transformerClass(list, clazz);
        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
    }

    public static List<SearchItem> getSearchItems(Map map){
        List list = new ArrayList();
        for (Object key : map.keySet()) {
            String param = (String)key;
            String[] params =  param.split("\\_");
            if(params.length>1){
                list.add(new SearchItem(params[0], (String) map.get(key),params[1]));
            }else{
                list.add(new SearchItem(params[0], (String) map.get(key),"like"));
            }
        }
        return list;
    }

    @Data
    @AllArgsConstructor
    static class Pagination{
        private int pageSize;
        private int current;
        private long total;
    }

}