package org.naruto.framework.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Transformer;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ObjUtils {

    public static Object copyMap2Obj(Map map, Class clazz){
        try {
            Object obj = clazz.newInstance();
            return copyMap2Obj(map,obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object copyMap2Obj(Map map, Object target){

        Set set = map.keySet();
        List<String> tList = getPropertyList(target);
        Collection collection = CollectionUtils.intersection(set,tList);

        log.info("same property {}",collection.toString());
        for (Object o : collection) {
            String property = (String)o;
            Object value = map.get(property);
            try {
                Field f = target.getClass().getDeclaredField(property);
                if(f.getType() == Date.class){
                    value = new Date((long)value);
                }
                f.setAccessible(true);
                f.set(target, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return target;
    }

    private static List<String> getPropertyList(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        List fieldList = Arrays.asList(fields);
        List<String> pList = (List<String>) CollectionUtils.collect(fieldList, new Transformer() {
            @Override
            public Object transform(Object o) {
                Field user = (Field) o;
                return user.getName();
            }
        });
        log.info("same property {}",pList.toString());
        return pList;
    }

    private static List<String> getSamePropertyList(Object obj1,Object obj2){
        List<String> srcPList = getPropertyList(obj1);
        List<String> targetPList = getPropertyList(obj2);
        List<String> pList = ListUtils.intersection(srcPList,targetPList);
        return pList;
    }

    public static void copyProperties(Object src,Object target)  {
        List<String> pList =  getSamePropertyList(src,target);
        for (String property : pList) {
            try {
                Field sField = src.getClass().getDeclaredField(property);
                sField.setAccessible(true);
                Object value = sField.get(src);
                Field tField = target.getClass().getDeclaredField(property);
                tField.setAccessible(true);
                tField.set(target,value);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object convert(Object src,Class clazz)  {
        try {
            Object instance = clazz.newInstance();
            copyProperties(src,instance);
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List transformerClass(List srcList, Class clazz)  {
        List list = new ArrayList();

        for (Object o : srcList) {
            try {
                Object instance = clazz.newInstance();
                copyProperties(o,instance);
                list.add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
