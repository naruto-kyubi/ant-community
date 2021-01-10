package org.naruto.framework.investment.service;

import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountRepository;
import org.naruto.framework.investment.repository.AccountType;
import org.naruto.framework.investment.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

@Scope("prototype")
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private AccountOperation accountOperation;

    public List<Account> queryAccountByOwner(String owner){
        return accountRepository.queryAccountByOwner(owner);
    }

    public List<Account> queryAccountsByParentAndType(String owner,String parent,String type){return accountRepository.queryAccountsByParentAndType(owner, parent, type);}

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

        account.setLastOperationAt(new Date());
        account.setLastOperationStatus("1");
        accountRepository.save(account);
        return account;

    }

    public List<AccountType> queryAccountType(){
        return accountTypeRepository.findAll();
    }

}
