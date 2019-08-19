package org.naruto.framework.core.utils;

import java.lang.reflect.Field;
import java.util.*;

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

    public static Object copyMap2Obj(Map map, Object obj){
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            try {
                Field f = obj.getClass().getDeclaredField(key);
                if(f.getType() == Date.class){
                    value = new Date((long)value);
                }
                f.setAccessible(true);
                f.set(obj, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static void copyProperties(Object src,Object target)  {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Field sField = src.getClass().getDeclaredField(field.getName());
                sField.setAccessible(true);
                Object value = sField.get(src);

                field.setAccessible(true);
                field.set(target,value);
            } catch (Exception e) {
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
