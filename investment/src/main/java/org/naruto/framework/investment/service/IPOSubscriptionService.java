package org.naruto.framework.investment.service;

import lombok.extern.java.Log;
import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.IPOSubscriptionRepository;
import org.naruto.framework.investment.repository.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Scope("prototype")
@Service
public class IPOSubscriptionService {

    private AccountOperation accountOperation;

    @Autowired
    private PythonOperation pythonOperation;
    @Autowired
    private IPOSubscriptionRepository ipoSubscriptionRepository;

    public List<IPOSubscription> findIPOSubscriptions(String stockCode, String nameCn, String type){

        return ipoSubscriptionRepository.findIPOSubscriptions(stockCode,nameCn,type);
    }

    public IPOSubscription findIPOSubscriptionById(String id){
        return ipoSubscriptionRepository.findById(id).get();
    }

    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        try {
            Account account = ipoSubscription.getAccount();
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());
        } catch (Exception e) {
//            e.printStackTrace();
            accountOperation = pythonOperation;
        }
        return accountOperation.oneCash(ipoSubscription,stock);
    }

    public IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        try {
            Account account = ipoSubscription.getAccount();
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());
        } catch (Exception e) {
//            e.printStackTrace();
            accountOperation = pythonOperation;
        }
        ipoSubscription =  accountOperation.sign(ipoSubscription,stock);
        ipoSubscriptionRepository.save(ipoSubscription);

        return ipoSubscription;
    }

}
