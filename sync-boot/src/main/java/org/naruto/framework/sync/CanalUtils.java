package org.naruto.framework.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

public class CanalUtils {

    public static String getColumnValue(List<CanalEntry.Column> columnList, String key){

        for (CanalEntry.Column column : columnList) {
            String name = column.getName();
            String value = column.getValue();
            if(key.equals(name)){
                return value;
            }
        }
        return null;
    }
}
