package org.naruto.framework.core.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerSpecs {

    public static <T> Specification<T> createQuery(List<SearchItem> searchItems){

        return new Specification<T>(){
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                EntityType<T> type = root.getModel();


                for (SearchItem item : searchItems) {
                    String key = item.getKey();

                    String[] strs = key.split("\\.");
                    Path path = null;
                    if(strs.length>1) {
                        //嵌套对象；
//                        String cls = str[0];
//                        String id = str[1];
                        for (String s : strs) {
                            if (null == path) {
                                path = root.get(s);
                                continue;
                            }
                            path = path.get(s);
                        }

                    }else {
                        path = root.get(item.getKey());
                    }
                    if ("equal".equalsIgnoreCase(item.getRule())) {
                        predicates.add(criteriaBuilder.equal(path,item.getValue()));

                    } else if ("like".equalsIgnoreCase(item.getRule())) {
                        predicates.add(criteriaBuilder.like(path, "%" + item.getValue() + "%"));
                    } else if ("between".equalsIgnoreCase(item.getRule())) {
                        //解析value值，数组；
                        String value = item.getValue();
                        String[] values = value.split(",");
                        try {
                            Field field = type.getJavaType().getDeclaredField(item.getKey());
                            if(field.getType()==Date.class){
                                if (values.length > 1) {

                                    if (StringUtils.isNotEmpty(values[0]) && StringUtils.isNotEmpty(values[1])) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date fromDate = sdf.parse(values[0]);
                                        Date toDate = sdf.parse(values[1]);
                                        predicates.add(criteriaBuilder.between(root.get(item.getKey()),fromDate, toDate));
                                    } else if (StringUtils.isNotEmpty(values[0])) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date fromDate = sdf.parse(values[0]);
                                        predicates.add(criteriaBuilder.greaterThan(root.get(item.getKey()), fromDate));
                                    } else if (StringUtils.isNotEmpty(values[1])) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date toDate = sdf.parse(values[1]);
                                        predicates.add(criteriaBuilder.lessThan(root.get(item.getKey()), toDate));
                                    }
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date fromDate = sdf.parse(values[0]);
                                    predicates.add(criteriaBuilder.greaterThan(path, fromDate));
                                }

                            }else{
                                //字符串；
                                if (values.length > 1) {
                                    if (StringUtils.isNotEmpty(values[0]) && StringUtils.isNotEmpty(values[1])) {
                                        predicates.add(criteriaBuilder.between(root.get(item.getKey()), values[0], values[1]));
                                    } else if (StringUtils.isNotEmpty(values[0])) {
                                        predicates.add(criteriaBuilder.greaterThan(root.get(item.getKey()), values[0]));
                                    } else if (StringUtils.isNotEmpty(values[1])) {
                                        predicates.add(criteriaBuilder.lessThan(root.get(item.getKey()), values[1]));
                                    }
                                } else {
                                    predicates.add(criteriaBuilder.greaterThan(path, values[0]));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
