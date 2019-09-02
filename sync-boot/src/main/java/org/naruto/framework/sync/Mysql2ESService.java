package org.naruto.framework.sync;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Component
public class Mysql2ESService {

    @Autowired
    private ArticleSyncService articleSyncService;

    @Autowired
    private ArticleTagSyncService articleTagSyncService;

    @Autowired
    private UserSyncService userSyncService;

    @Autowired
    private CanalConfig canalConfig;

    public  void sync(){
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConfig.getAddress(),
                canalConfig.getPort()), canalConfig.getDestination(), canalConfig.getUsername(), canalConfig.getPassword());
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    emptyCount = 0;
                    syncTableData(message.getEntries());
                }
                connector.ack(batchId); // 提交确认
            }

        } finally {
            connector.disconnect();
        }
    }


    private void syncTableData(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();

            log.info(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            String db = entry.getHeader().getSchemaName();
            String table = entry.getHeader().getTableName();

            if(canalConfig.getDatabase().equals(db)){
                if("articles".equals(table)){
                    //同步mysql的artilces表的数据；
                    articleSyncService.sync(rowChage);
                }else if("article_tags".equals(table)){
                    //同步mysql的aritlce_tags表的数据;
                    articleTagSyncService.sync(rowChage);
                }else if("users".equals(table)){
                    //同步mysql的users表的数据
                    userSyncService.sync(rowChage);
                }
            }
        }
    }
}
