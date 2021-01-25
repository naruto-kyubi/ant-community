package org.naruto.framework.investment.service;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.investment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    @Autowired
    private FundTransRepository fundTransRepository;

    private AccountOperation accountOperation;

    @Autowired
    private PythonOperation pythonOperation;

    public List<Account> queryMainAccountByOwner(String owner){
        return accountRepository.queryMainAccountByOwner(owner);
    }

    public List<Account> queryAccounts(String owner,String parent){

        return accountRepository.findAccountsByOwnerAndParentNot(owner,parent);
    }

    public List<Account> queryAccountsByParentAndType(String owner,String parent,String type){return accountRepository.queryAccountsByParentAndType(owner, parent, type);}

    public Account queryAccountById(String id){
        return accountRepository.queryAccountById(id);
    }

    public Account connect(String id) {
        Account account = accountRepository.queryAccountById(id);

        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId());
        } catch (Exception e) {
            e.printStackTrace();
            accountOperation = pythonOperation;
        }
        try {
            accountOperation.connect(account);
            account.setLastOperationStatus("1");
        } catch (Exception e) {
            account.setLastOperationStatus("0");
        }
        //保存操作结果
        account.setLastOperationAt(new Date());
        return account;
    }

    public Account QueryBalance(String id)  {

        Account account = accountRepository.queryAccountById(id);

        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId());
        } catch (Exception e) {
            e.printStackTrace();
            accountOperation = pythonOperation;
        }

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
        Sort sort = new Sort(Sort.Direction.ASC, "sn");
        return accountTypeRepository.findAll(sort);
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

        Account parent = accountRepository.getOne(account.getParent());
        //保存前端提交数据
        accountDb.setParent(account.getParent());
        accountDb.setNameEn(parent.getNameEn());
        accountDb.setNameCn(parent.getNameCn());
        accountDb.setAccountType(account.getAccountType());
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

    public FundTrans addTrans(FundTrans fundTrans) {

        String accountId = fundTrans.getAccount();
        Account account = accountRepository.queryAccountById(accountId);
        fundTrans.setCurrency("HKD");
        fundTrans.setStatus(FundTransStatus.PLANNING.ordinal());
        fundTrans.setBalanceBeforeTrans(account.getBalance());
        fundTrans.setTransAt(new Date());
        fundTransRepository.save(fundTrans);
        return fundTrans;
    }

    public FundTrans executeTrans(String id) {
        FundTrans fundTrans = fundTransRepository.getOne(id);
        Account account = accountRepository.getOne(fundTrans.getAccount());
        Account mainAccount = accountRepository.getOne(account.getParent());
        Account bankAccount = accountRepository.getOne(mainAccount.getBankAccount());

        try {
            accountOperation = (AccountOperation) SpringUtils.getBean(account.getAccountType().getId());
        } catch (Exception e) {
            e.printStackTrace();
            accountOperation = pythonOperation;
        }

        try {
            fundTrans = accountOperation.executeTrans(fundTrans,account,bankAccount);
            fundTrans.setStatus(FundTransStatus.SUCCEED.ordinal());
        } catch (Exception e) {
            fundTrans.setStatus(FundTransStatus.ERROR.ordinal());
        }
        //保存操作结果
        fundTrans.setTransAt(new Date());
        fundTransRepository.save(fundTrans);
        return fundTrans;

    }

    public FundTrans closeTrans(String id) {
        FundTrans fundTrans = fundTransRepository.getOne(id);
        fundTrans.setStatus(FundTransStatus.FINISHED.ordinal());
        fundTransRepository.save(fundTrans);
        return fundTrans;
    }
}


