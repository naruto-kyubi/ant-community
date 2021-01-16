package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountType;
import org.naruto.framework.investment.service.AccountService;
import org.naruto.framework.investment.service.StockService;
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
public class StockController {

    @Autowired
    private StockService stockService;

    @ResponseBody
    @RequestMapping(value = "/v1/stocks", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryStocks(
            HttpServletRequest request, HttpServletResponse response) {
        List stocks = stockService.findAll();
        return ResponseEntity.ok(ResultEntity.ok(stocks));
    }
}
