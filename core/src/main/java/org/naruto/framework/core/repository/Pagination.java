package org.naruto.framework.core.repository;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class Pagination {
    private String sorter;
    private int current = 0;
    private int pageSize =0;

    public Pageable getPageable(){
        int current = this.getCurrent();

        if(current==0) current=1;
        current = current - 1;

        int pageSize = this.getPageSize();
        String sorter = this.getSorter();

        if(pageSize<1) pageSize = 10;
        //分页信息
        Pageable pageable = PageRequest.of(current, pageSize);

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
            pageable = PageRequest.of(current, pageSize, sort);
        }
        return pageable;
    }
}
