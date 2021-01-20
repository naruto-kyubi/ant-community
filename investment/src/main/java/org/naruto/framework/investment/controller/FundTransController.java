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


}
