package org.naruto.framework.investment.service;

import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.Account;
import org.springframework.beans.factory.annotation.Autowired;

public class InvestmentBaseService {

    @Autowired
    PythonOperation pythonOperation;

    AccountOperation accountOperation;

    public void setOperationByAccount(Account account){

        // 首先去带 “_” 的，找不到再找其他
        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId().concat("_"));
            return;
        } catch (Exception e) {

        }

        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId());
        } catch (Exception e) {
            e.printStackTrace();
            accountOperation = pythonOperation;
        }
    }
}
