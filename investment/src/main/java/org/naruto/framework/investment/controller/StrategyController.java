package org.naruto.framework.investment.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.BondDoubleLowResult;
import org.naruto.framework.investment.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log
@RestController
public class StrategyController {
    @Autowired
    private StrategyService strategyService;

    @ResponseBody
    @RequestMapping(value = "/v1/bondDoubleLowResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> querySubAccounts(
            @RequestParam(required = false) String owner,
            HttpServletRequest request, HttpServletResponse response) {

        List<BondDoubleLowResult> bondDoubleLowResults = strategyService.findAll();
        return ResponseEntity.ok(ResultEntity.ok(bondDoubleLowResults));
    }
}
