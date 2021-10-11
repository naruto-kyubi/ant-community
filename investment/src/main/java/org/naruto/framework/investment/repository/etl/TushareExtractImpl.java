package org.naruto.framework.investment.repository.etl;

import org.springframework.stereotype.Component;

@Component
public class TushareExtractImpl implements Extract {

    @Override
    public void extractList(ExtractRequest extractRequest) {
        //extractRequest 参数（params）中的 start_date，如果没有话查找数据库中最后一次更新的时间，如果没有默认2010年1月1日，开始按周进行获取；
    }

    @Override
    public void extractSingleObject(ExtractRequest extractRequest) {

    }
}
