package org.naruto.framework.investment.controller;

import lombok.extern.flogger.Flogger;
import lombok.extern.java.Log;
import org.naruto.framework.core.SpringUtils;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountType;
import org.naruto.framework.investment.service.AccountService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.List;

@Log
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired(required = false)
    private ISessionService sessionService;

    @ResponseBody
    @RequestMapping(value = "/v1/subAccounts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> querySubAccounts(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String parent,
            @RequestParam(required = false) String type,
            HttpServletRequest request, HttpServletResponse response) {

        User user = (User) sessionService.getCurrentUser(request);
        List<Account> accounts = accountService.queryAccountsByParentAndType(user.getId(),parent,type);
        return ResponseEntity.ok(ResultEntity.ok(accounts));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/mainAccounts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryMainAccounts(
            @RequestParam(required = false) String owner,
            HttpServletRequest request, HttpServletResponse response) {

        User user = (User) sessionService.getCurrentUser(request);
        List<Account> accounts = accountService.queryMainAccountByOwner(user.getId());
        return ResponseEntity.ok(ResultEntity.ok(accounts));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/connect", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> connect(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        Account account = accountService.connect(id);
        return ResponseEntity.ok(ResultEntity.ok(account));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/dayEndClearing", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> dayEndClearing(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {
          User user = (User) sessionService.getCurrentUser(request);
          String owner = user.getId();
          accountService.dayEndClearing(owner);
          return ResponseEntity.ok(ResultEntity.ok(null));
    }



    @ResponseBody
    @RequestMapping(value = "/v1/queryBalance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryBalance(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        Account account = accountService.QueryBalance(id);
        return ResponseEntity.ok(ResultEntity.ok(account));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/queryAccountTypes", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> query(
            HttpServletRequest request, HttpServletResponse response) {

        List<AccountType> accountTypes = accountService.queryAccountType();
        return ResponseEntity.ok(ResultEntity.ok(accountTypes));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/addAccount", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> add(@Validated @RequestBody  Account account, HttpServletRequest request){

        return ResponseEntity.ok(ResultEntity.ok(accountService.addAccount(account)));

    }

    @ResponseBody
    @RequestMapping(value = "/v1/updateAccount", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> update(@Validated @RequestBody  Account account, HttpServletRequest request){
        System.out.println(account);
        return ResponseEntity.ok(ResultEntity.ok(accountService.updateAccount(account)));

    }

}
