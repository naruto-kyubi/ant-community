package org.naruto.framework.investment.repository.etl;

import com.alibaba.fastjson.JSON;
import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.FundNewShareAllocation;
import org.naruto.framework.investment.repository.FundNewShareAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Log
@Component
public class LoadingManager {

    @Autowired
    private FundNewShareAllocationRepository fundNewShareAllocationRepository;

    public void loading(String apiName, String data){
        switch(apiName){
            case "extractFundNewShareAllocation":
                this.tlFundNewShareAllocation(data);
                break;
            case "extractNewShare":
                this.tlNewShare(data);
            default:
                break;
        }
    }

    private void tlNewShare(String data) {
        // 记录获取最后一次完成新股
    }

    ;

    // transform and loading
    public String tlFundNewShareAllocation(String data){
        List transformResult = this.transform(data, FundNewShareAllocation.class,FundNewShareAllocation.eastMoneyTransformMap);

        for(Object fundNewShareAllocation:transformResult){

            FundNewShareAllocation fa = (FundNewShareAllocation)fundNewShareAllocation;
            String fCode = fa.getFCode();

            // 只存几只关注的基金；
            if( fCode.equals("515350")||fCode.equals("515930")||fCode.equals("515360")) {
                fundNewShareAllocationRepository.save(fa);
            }
        }
        return "OK";
    }


    public List transform(String data, Class targetClass, Map transformMap){

        Iterator it=transformMap.keySet().iterator();
        while(it.hasNext()){
            String key;
            String value;
            key=it.next().toString();
            value= (String) transformMap.get(key);
            data = data.replaceAll(key,value);
        }

        List targetList = JSON.parseArray(data,targetClass);
        return targetList;
    }
}
