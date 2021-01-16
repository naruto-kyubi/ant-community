package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.service.IPOSubscriptionService;
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

    @ResponseBody
    @RequestMapping(value = "/v1/ipoSubscriptions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryIPOSubscriptions(
            @RequestParam(required = true) String stockCode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            HttpServletRequest request, HttpServletResponse response) {
            if (null == name) name ="%";
            if(null==type) type="%";
        List<IPOSubscription> list =  ipoSubscriptionService.findIPOSubscriptions(stockCode,name,type);

        List resultList = new ArrayList();

        for(IPOSubscription item : list){
            Account account = item.getAccount();
            IPOResult result = new IPOResult();
            result.setStockCode(item.getStock().getCode());
            result.setAppLocation(account.getAppLocation());
            result.setNameCn(account.getNameCn());
            result.setType(account.getType());

            result.setBalance(account.getBalance());
            result.setNumberOfShares(item.getNumberOfShares());
            result.setSubscriptionFee(item.getSubscriptionFee());
            result.setNumberOfSigned(item.getNumberOfSigned());
            resultList.add(result);
        }

        return ResponseEntity.ok(ResultEntity.ok(resultList));
    }
}
