package org.naruto.framework.investment.service;

import lombok.extern.java.Log;
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

@Log
@Scope("prototype")
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private AccountOperation accountOperation;

    public List<Account> queryMainAccountByOwner(String owner){
        return accountRepository.queryMainAccountByOwner(owner);
    }

    public List<Account> queryAccountsByParentAndType(String owner,String parent,String type){return accountRepository.queryAccountsByParentAndType(owner, parent, type);}

    public Account queryAccountById(String id){
        return accountRepository.queryAccountById(id);
    }

    public Account connect(String id) {
        Account account = accountRepository.queryAccountById(id);
        accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());
        try {
            accountOperation.connect(account);
            account.setLastOperationStatus("1");
        } catch (Exception e) {
            account.setLastOperationStatus("0");
        }
        //保存操作结果
        account.setLastOperationAt(new Date());
        accountRepository.save(account);
        return account;
    }

    public Account QueryBalance(String id) throws MalformedURLException, InterruptedException {
        Account account = accountRepository.queryAccountById(id);
        accountOperation = (AccountOperation) SpringUtils.getBean(account.getType());

        try {
            account = accountOperation.queryBalance(account);
            account.setLastOperationStatus("1");
        } catch (Exception e) {
            account.setLastOperationStatus("0");
        }
        //保存操作结果
        account.setLastOperationAt(new Date());
        accountRepository.save(account);
        return account;

    }

    public List<AccountType> queryAccountType(){
        return accountTypeRepository.findAll();
    }

    public Account addAccount(Account account){
        Account parent = accountRepository.queryAccountById(account.getParent());
        account.setOwner(parent.getOwner());
        account.setNameCn(parent.getNameCn());
        account.setNameEn(parent.getNameEn());

        if(account.getLoginId()==null||account.getLoginId().equals("")) account.setLoginId(parent.getMobile());
        if(account.getLoginPwd()==null||account.getLoginPwd().equals("")) account.setLoginPwd(parent.getLoginPwd());
        if(account.getAppLocation()==null||account.getAppLocation().equals("")) account.setAppLocation(parent.getAppLocation());
        if(account.getTradePwd()==null||account.getTradePwd().equals("")) account.setTradePwd(parent.getTradePwd());
        accountRepository.save(account);

        return account;
    }

    public Account updateAccount(Account account){
       // log.info(account.toString());
        Account accountDb = accountRepository.queryAccountById(account.getId());

        //保存前端提交数据
        accountDb.setParent(account.getParent());
        accountDb.setType(account.getType());
        accountDb.setAppLocation(account.getAppLocation());
        accountDb.setLoginPwd(account.getLoginPwd());
        accountDb.setTradePwd(account.getTradePwd());
        accountDb.setBalance(account.getBalance());
        accountDb.setLoginId(account.getLoginId());
        accountDb.setAccountNo(account.getAccountNo());

      //  log.info(accountDb.toString());

        accountRepository.save(accountDb);
        return accountDb;
    }

}
