package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountType;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;


@Log
@RestController
public class FundTransController {

    @Autowired
    private AccountService accountService;

    @ResponseBody
    @RequestMapping(value = "/v1/addTrans", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> add(@Validated @RequestBody FundTrans fundTrans, HttpServletRequest request){

        return ResponseEntity.ok(ResultEntity.ok(accountService.addTrans(fundTrans)));

    }

    @ResponseBody
    @RequestMapping(value = "/v1/executeTrans", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> executeTrans(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        accountService.executeTrans(id);
        return ResponseEntity.ok(ResultEntity.ok(""));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/closeTrans", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> closeTrans(
            @RequestParam(required = false) String id,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        accountService.closeTrans(id);
        return ResponseEntity.ok(ResultEntity.ok(""));
    }


}
