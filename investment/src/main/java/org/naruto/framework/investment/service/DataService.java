package org.naruto.framework.investment.service;

import org.naruto.framework.investment.repository.etl.EastmoneyExtractImpl;
import org.naruto.framework.investment.repository.etl.ExtractRequest;
import org.naruto.framework.investment.repository.etl.TushareExtractImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
    EastmoneyExtractImpl emExtract;

    @Autowired
    TushareExtractImpl tushareExtract;

    public void extractFundNewShareAllocation(String stockdCode){

        ExtractRequest extractRequest = new ExtractRequest();
        String url = "https://fund.eastmoney.com/api/FundDXGJJ.ashx?r=1633472405000&m=4&pageIndex={pageIndex}&sfname=FCODE&sorttype=desc&stkcode={stockCode}&IsSale=0&IDEL=1&_=1633472405479";
        extractRequest.setResourceUrl(url);
        extractRequest.getParams().put("pageIndex", 0);
        extractRequest.getParams().put("stockCode", stockdCode);
        extractRequest.setApiName("extractFundNewShareAllocation");

        emExtract.extractList(extractRequest);
    }


    public void extractNewShareBasic(){
//        ExtractRequest extractRequest = new ExtractRequest();
//        String url = "\thttps://fund.eastmoney.com/API/FundDXGJJ.ashx?r=1633505836000&m=2&pageIndex={pageIndex}&SFName=STKCODE&sorttype=desc&_=1633505836108";
//        extractRequest.setResourceUrl(url);
//        extractRequest.getParams().put("pageIndex", 0);
//        extractRequest.setApiName("extractNewShare");
//
//        emExtract.extractList(extractRequest);
        ExtractRequest extractRequest = new ExtractRequest();
        extractRequest.setApiName("new_share");
        extractRequest.setToken("a014e7c2a9700eab3e91dced760d937108f3379754c5bb3a35f4712e");
        tushareExtract.extractList(extractRequest);
    }

}
