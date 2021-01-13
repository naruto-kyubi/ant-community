package org.naruto.framework.investment.service;

import org.naruto.framework.investment.repository.Account;

import java.net.MalformedURLException;

public interface AccountOperation {
    public void connect(Account account) throws Exception;
    public Account queryBalance(Account account) throws Exception;
}
