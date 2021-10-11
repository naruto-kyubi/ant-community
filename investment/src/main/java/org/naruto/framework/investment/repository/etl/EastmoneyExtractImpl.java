package org.naruto.framework.investment.repository.etl;

import org.springframework.stereotype.Component;

@Component
public class EastmoneyExtractImpl extends ExtractBasic {

    @Override
    public void extractList(ExtractRequest extractRequest) {
        this.setUp();
        EastmoneyExtractListResponse emResponse = restTemplate.getForObject(extractRequest.getResourceUrl(), EastmoneyExtractListResponse.class,extractRequest.getParams());
       // if(emResponse.getErrCode() != 0){return;}
        double pageSize = Math.ceil(emResponse.getTotalCount()/20);
        for(int i = 0; i < pageSize; i++){
            // pageindex + 1 进行获取
            emResponse = restTemplate.getForObject(extractRequest.getResourceUrl(), EastmoneyExtractListResponse.class,extractRequest.getParams());
            loadingManager.loading(extractRequest.getApiName(),emResponse.getDatas());

            extractRequest.getParams().replace("pageIndex",i+1);
        }
    }

    @Override
    public void extractSingleObject(ExtractRequest extractRequest) {

    }
}
