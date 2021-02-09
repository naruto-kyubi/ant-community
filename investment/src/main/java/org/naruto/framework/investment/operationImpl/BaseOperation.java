package org.naruto.framework.investment.operationImpl;

import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.connect.WebSessionManager;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.service.AccountOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseOperation implements AccountOperation {

    @Autowired
    SessionManager sessionManager;

    @Autowired
    WebSessionManager webSessionManager;


    @Override
    public void connect(Account account) throws Exception {

    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        return null;
    }

    @Override
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public FundTrans executeTrans(FundTrans fundTrans, Account account, Account BankAccount) throws Exception {
        return null;
    }

    @Override
    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {

    }

    @Override
    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {

    }

    @Override
    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public IPOSubscription cancelFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Account account = ipoSubscription.getAccount();
        this.webSessionManager.addStopSing(account.getAppLocation());
        return ipoSubscription;
    }
}
