package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;

import java.util.List;


public interface AccountRepository extends CustomRepository<Account,String>{
    public List<Account> queryAccountByOwner(String owner);
    public Account queryAccountById(String id);

}