package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.service.AccountService;
import org.naruto.framework.investment.service.IPOSubscriptionService;
import org.naruto.framework.investment.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log
@RestController
public class IPOSubscriptionController {

    @Autowired
    private IPOSubscriptionService ipoSubscriptionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StockService stockService;

    @ResponseBody
    @RequestMapping(value = "/v1/ipoSubscriptions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryIPOSubscriptions(
            @RequestParam(required = true) String stockCode,
            HttpServletRequest request, HttpServletResponse response) {

        List<IPOSubscription> list =  ipoSubscriptionService.findIPOSubscriptions(stockCode);

        List resultList = new ArrayList();

        for(IPOSubscription item : list){
            Account account = item.getAccount();
            IPOResult result = new IPOResult();
            result.setId(item.getId());
            result.setStockCode(item.getStock().getCode());
            result.setAppLocation(account.getAppLocation());
            result.setNameCn(account.getNameCn());
            result.setType(account.getAccountType().getId());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
            resultList.add(result);
        }

        return ResponseEntity.ok(ResultEntity.ok(resultList));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/addPlan", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addPlan(
            @RequestParam(required = true) String stockCode,
            @RequestParam(required = true) String id,
            HttpServletRequest request, HttpServletResponse response) {

        Stock stock = stockService.queryStockByCode(stockCode);
        IPOSubscription ipo = ipoSubscriptionService.findIPOSubscriptionById(id);
        IPOResult result = new IPOResult();
        try {
            IPOSubscription item = ipoSubscriptionService.addPlan(ipo,stock);
            Account account = item.getAccount();

            result.setId(item.getId());
            result.setStockCode(item.getStock().getCode());
            result.setAppLocation(account.getAppLocation());
            result.setNameCn(account.getNameCn());
            result.setType(account.getAccountType().getId());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(result));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/ipo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> ipo(
            @RequestParam(required = true) String stockCode,
            @RequestParam(required = true) String id,
            HttpServletRequest request, HttpServletResponse response) {

        Stock stock = stockService.queryStockByCode(stockCode);
        IPOSubscription ipo = ipoSubscriptionService.findIPOSubscriptionById(id);
        IPOResult result = new IPOResult();
        try {
            IPOSubscription item = ipoSubscriptionService.oneCash(ipo,stock);
            Account account = item.getAccount();

            result.setId(item.getId());
            result.setStockCode(item.getStock().getCode());
            result.setAppLocation(account.getAppLocation());
            result.setNameCn(account.getNameCn());
            result.setType(account.getAccountType().getId());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(result));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/sign", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> sign(
            @RequestParam(required = true) String stockCode,
            @RequestParam(required = true) String id,
            HttpServletRequest request, HttpServletResponse response) {

        Stock stock = stockService.queryStockByCode(stockCode);
        IPOSubscription ipo = ipoSubscriptionService.findIPOSubscriptionById(id);
        IPOResult result = new IPOResult();
        try {
            IPOSubscription item = ipoSubscriptionService.sign(ipo,stock);
            Account account = item.getAccount();

            result.setId(item.getId());
            result.setStockCode(item.getStock().getCode());
            result.setAppLocation(account.getAppLocation());
            result.setNameCn(account.getNameCn());
            result.setType(account.getAccountType().getId());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(result));
    }
}
