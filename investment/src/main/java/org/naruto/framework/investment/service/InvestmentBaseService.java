package org.naruto.framework.investment.service;

import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.Account;
import org.springframework.beans.factory.annotation.Autowired;

public class InvestmentBaseService {

    @Autowired
    PythonOperation pythonOperation;

    AccountOperation accountOperation;

    public void setOperationByAccount(Account account){
        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId());
        } catch (Exception e) {
            e.printStackTrace();
            accountOperation = pythonOperation;
        }
    }
}
