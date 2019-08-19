package org.naruto.framework.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.service.UserEsService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSyncService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEsService userEsService;

    public void sync(CanalEntry.RowChange rowChange){
        CanalEntry.EventType eventType = rowChange.getEventType();

        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            if (eventType == CanalEntry.EventType.DELETE) {

                String value = CanalUtils.getColumnValue(rowData.getBeforeColumnsList(),"id");
                userEsService.delete(value);
            } else{

                String value = CanalUtils.getColumnValue(rowData.getAfterColumnsList(),"id");
                User user = userService.queryUserById(value);
                EsUser esUser = (EsUser) ObjUtils.convert(user,EsUser.class);
                userEsService.save(esUser);
            }
        }
    }

}
