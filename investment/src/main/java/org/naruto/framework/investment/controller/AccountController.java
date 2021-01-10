package org.naruto.framework.investment.controller;

import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountType;
import org.naruto.framework.investment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ResponseBody
    @RequestMapping(value = "/v1/mainAccounts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) String owner,
            HttpServletRequest request, HttpServletResponse response) {

        List<Account> accounts = accountService.queryAccountByOwner(owner);
        return ResponseEntity.ok(ResultEntity.ok(accounts));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/connect", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> connect(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        accountService.connect(id);
        return ResponseEntity.ok(ResultEntity.ok(""));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/queryBalance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryBalance(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        accountService.QueryBalance(id);
        return ResponseEntity.ok(ResultEntity.ok(""));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/queryAccountTypes", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> query(
            HttpServletRequest request, HttpServletResponse response) {

        List<AccountType> accountTypes = accountService.queryAccountType();
        return ResponseEntity.ok(ResultEntity.ok(accountTypes));
    }

}
