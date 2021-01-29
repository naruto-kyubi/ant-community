package org.naruto.framework.investment.service;

import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;

import java.net.MalformedURLException;

public interface AccountOperation {
    public void connect(Account account) throws Exception;
    public Account queryBalance(Account account) throws Exception;
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception;

    IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception;

    public FundTrans executeTrans(FundTrans fundTrans,Account account,Account BankAccount) throws Exception;

    IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock)  throws Exception;

    IPOSubscription cancelFinanceIPO(IPOSubscription ipoSubscription, Stock stock)  throws Exception;

}
