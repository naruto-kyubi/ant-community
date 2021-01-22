package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.service.AccountService;
import org.naruto.framework.investment.service.IPOSubscriptionService;
import org.naruto.framework.investment.service.StockService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired(required = false)
    private ISessionService sessionService;

    @ResponseBody
    @RequestMapping(value = "/v1/importData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> importData(
            @RequestParam(required = true) String stockCode,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            Stock stock = stockService.queryStockByCode(stockCode);

            User user = (User) sessionService.getCurrentUser(request);
            String owner = user.getId();
            List<Account> accountList = accountService.queryAccounts(owner,"-1");
            List<IPOSubscription> ipoList = new ArrayList();
            for(Account account : accountList){
                //手续费
                Float commissionFee = account.getAccountType().getCommissionFee();
                //入场费
                Float adminssionFee = stock.getAdmissionFee();

                //申购所需费用
                float value = commissionFee + adminssionFee;
                IPOSubscription ipo = new IPOSubscription();
                ipo.setAccount(account);
                ipo.setStock(stock);
                ipo.setSubscriptionFee(value);
//                ipo.setNumberOfShares(-1);
//                ipo.setNumberOfSigned(-1);
//                ipo.setPlanIPO(-2);
                ipo.setLastOperationAt(new Date());
                ipoList.add(ipo);
            }

            ipoSubscriptionService.save(ipoList,stock,stock.getCode());
            List<IPOSubscription> list =  ipoSubscriptionService.findIPOSubscriptions(stockCode);

            List resultList = new ArrayList();

            for(IPOSubscription item : list){
                Account account = item.getAccount();
                IPOResult result = new IPOResult();
                result.setId(item.getId());
                result.setStockCode(item.getStock().getCode());
                result.setAppLocation(account.getAppLocation());
                result.setNameCn(account.getNameCn());
                result.setType(account.getAccountType().getNameCn());

                result.setPlanIPO(item.getPlanIPO());
                result.setBalance(account.getBalance());
                result.setSubscriptionFee(item.getSubscriptionFee());
                result.setNumberOfShares(item.getNumberOfShares());
                result.setSubscriptionFee(item.getSubscriptionFee());
                result.setNumberOfSigned(item.getNumberOfSigned());
                resultList.add(result);
            }

            return ResponseEntity.ok(ResultEntity.ok(resultList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(new ArrayList()));
    }


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
            result.setType(account.getAccountType().getNameCn());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setSubscriptionFee(item.getSubscriptionFee());
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
            result.setType(account.getAccountType().getNameCn());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setSubscriptionFee(item.getSubscriptionFee());
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
            result.setType(account.getAccountType().getNameCn());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setSubscriptionFee(item.getSubscriptionFee());
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
            result.setType(account.getAccountType().getNameCn());

            result.setPlanIPO(item.getPlanIPO());
            result.setBalance(account.getBalance());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(ResultEntity.ok(result));
    }
}
