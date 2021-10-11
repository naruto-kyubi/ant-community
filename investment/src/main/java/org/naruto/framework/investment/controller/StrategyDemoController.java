package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.StockValuationDashborard;
import org.naruto.framework.investment.service.StrategyDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log
@RestController
public class StrategyDemoController {
    @Autowired
    private StrategyDemoService strategyDemoService;

    @ResponseBody
    @RequestMapping(value = "/v1/stockValuationDashborard", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryStockValuationDashborard(
            @RequestParam(required = false) String owner,
            HttpServletRequest request, HttpServletResponse response) {

        List<StockValuationDashborard> stockValuationDashborards = strategyDemoService.queryStockValuationDashborard();
        return ResponseEntity.ok(ResultEntity.ok(stockValuationDashborards));
    }
}
