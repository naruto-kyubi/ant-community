package org.naruto.framework.investment.service;

import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private AccountOperation accountOperation;

    public List<Account> queryAccountByOwner(String owner){
        return accountRepository.queryAccountByOwner(owner);
    }

    public Account queryAccountById(String id){
        return accountRepository.queryAccountById(id);
    }

    public Account connect(String id) throws MalformedURLException, InterruptedException{
        Account account = accountRepository.queryAccountById(id);
        accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());
        accountOperation.connect(account);
        //保存操作结果
        account.setLastOperationStatus("1");
        accountRepository.save(account);
        return account;
    }

    public Account QueryBalance(String id) throws MalformedURLException, InterruptedException {
        Account account = accountRepository.queryAccountById(id);
        accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());
        account = accountOperation.queryBalance(account);

        account.setLastOperationStatus("1");
        accountRepository.save(account);
        return account;

    }

}
