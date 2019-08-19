package org.naruto.framework.core.repository;

import org.naruto.framework.core.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CustomRepositoryImpl <T,ID extends Serializable>
        extends SimpleJpaRepository<T,ID>
        implements CustomRepository<T,ID>{

//    @PersistenceContext
    EntityManager entityManager;

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager){
        super(domainClass,entityManager);
        this.entityManager = entityManager;

    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> queryPageByCondition(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        _map = PageUtils.clearPaginationArgs(_map);
        List<SearchItem> searchItems = PageUtils.getSearchItems(_map);
        return (Page<T>) findAll(CustomerSpecs.createQuery(searchItems),pageable);
    }

    @Transactional
    @Modifying
    @Override
    public void increateCount(String id, String columnName, Long step) {
        String sqlTableName = ((Table)this.getDomainClass().getAnnotation(Table.class)).name();

        Field filed = getIdField(this.getDomainClass());
        String idColumnName = ((Column)filed.getAnnotation(Column.class)).name();
        if(null==idColumnName || "".equals(idColumnName)) idColumnName = filed.getName();
        String sql = "update " + sqlTableName + " set " + columnName + "=" + columnName + "+" + step + " where " + idColumnName + "='" + id +"'";
        Query query = entityManager.createNativeQuery(sql);

        query.executeUpdate();
    }

    public static Field getIdField(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        Field item = null;
        for (Field field : fields) {
            //获取实体类中标识@Id的字段
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                field.setAccessible(true);
                item = field;
                break;
            }
        }
        if(item==null){
            Class<?> superclass = clazz.getSuperclass();
            if(superclass!=null){
                item = getIdField(superclass);
            }
        }
        return item;
    }
}
