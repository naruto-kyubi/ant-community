package org.naruto.framework.investment.service;

import lombok.extern.java.Log;
import org.apache.commons.collections.ListUtils;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.IPOSubscriptionRepository;
import org.naruto.framework.investment.repository.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Log
@Scope("prototype")
@Service
public class IPOSubscriptionService extends InvestmentBaseService{

    @Autowired
    private IPOSubscriptionRepository ipoSubscriptionRepository;

    public List<IPOSubscription> findIPOSubscriptions(String stockId){

        return ipoSubscriptionRepository.findIPOSubscriptions(stockId);
    }

    public IPOSubscription findIPOSubscriptionById(String id){
        return ipoSubscriptionRepository.findById(id).get();
    }

    public IPOSubscription addPlan(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        ipoSubscription.setPlanSubscriptionShares(stock.getLot());
        ipoSubscription.setLastOperationAt(new Date());
        ipoSubscriptionRepository.save(ipoSubscription);
        return ipoSubscription;
    }

    public IPOSubscription removePlan(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        ipoSubscription.setPlanSubscriptionShares(0);
        ipoSubscription.setLastOperationAt(new Date());
        ipoSubscriptionRepository.save(ipoSubscription);
        return ipoSubscription;
    }

    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        this.setOperationByAccount(ipoSubscription.getAccount());

        IPOSubscription ipo = accountOperation.oneCash(ipoSubscription,stock);
        ipo.setLastOperationAt(new Date());
        ipoSubscriptionRepository.save(ipo);
        return ipo;
    }

    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception{

        this.setOperationByAccount(ipoSubscription.getAccount());
        accountOperation.logonFinanceIPO(ipoSubscription);
    }

    public void prepareFinanceIPO(IPOSubscription ipoSubscription,Stock stock) throws Exception{
        this.setOperationByAccount(ipoSubscription.getAccount());
        accountOperation.prepareFinanceIPO(ipoSubscription,stock);
    }

    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        this.setOperationByAccount(ipoSubscription.getAccount());

        IPOSubscription ipo = accountOperation.addFinanceIPO(ipoSubscription,stock);
        ipo.setLastOperationAt(new Date());
        return ipo;
    }

    public IPOSubscription cancelFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        this.setOperationByAccount(ipoSubscription.getAccount());

        IPOSubscription ipo = accountOperation.cancelFinanceIPO(ipoSubscription,stock);
        ipo.setLastOperationAt(new Date());
        return ipo;
    }

    public IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        this.setOperationByAccount(ipoSubscription.getAccount());
        ipoSubscription =  accountOperation.sign(ipoSubscription,stock);
        ipoSubscription.setLastOperationAt(new Date());
        ipoSubscriptionRepository.save(ipoSubscription);
        return ipoSubscription;
    }

    public List save(List<IPOSubscription> list,Stock stock,String stockId) throws Exception{
        List<IPOSubscription> ipoList = this.findIPOSubscriptions(stockId);

        List newList = ListUtils.subtract(list,ipoList);

        return ipoSubscriptionRepository.saveAll(newList);
    }

    public IPOSubscription update(IPOSubscription ipoSubscription) throws Exception {

        IPOSubscription ipo = this.findIPOSubscriptionById(ipoSubscription.getId());

        ipo.setSubscriptionType(ipoSubscription.getSubscriptionType());
        if("0".equals(ipoSubscription.getSubscriptionType())){
            //现金申购利息0；
            ipo.setInterest(0F);
        }else{
            //融资利息；
            ipo.setInterest(ipoSubscription.getInterest());
        }
        ipo.setCommissionFee(ipoSubscription.getCommissionFee());

        //申购费用=入场费+利息+手续费；
        float subscriptionFee = ipo.getStock().getAdmissionFee() + ipoSubscription.getInterest() + ipoSubscription.getCommissionFee();
        ipo.setSubscriptionFee(subscriptionFee);
        ipo.setPlanSubscriptionShares(ipoSubscription.getPlanSubscriptionShares());
        ipo.setNumberOfShares(ipoSubscription.getNumberOfShares());
        ipo.setNumberOfSigned(ipoSubscription.getNumberOfSigned());
        ipo.setLastOperationAt(new Date());
        return ipoSubscriptionRepository.save(ipo);
    }
}
