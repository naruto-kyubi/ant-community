package org.naruto.framework.investment.strategy.controller;

import lombok.extern.java.Log;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.strategy.repository.PortfolioDaily;
import org.naruto.framework.investment.strategy.repository.Strategy;
import org.naruto.framework.investment.strategy.service.StrategyService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Log
@RestController
public class StrategyController {
    @Autowired
    private StrategyService strategyService;

    @Autowired(required = false)
    private ISessionService sessionService;


    @ResponseBody
    @RequestMapping(value = "/v1/strategy/myStrategies", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> queryMyStrategies(
            @RequestParam(required = false) String owner,
            HttpServletRequest request, HttpServletResponse response) {
        User user = (User) sessionService.getCurrentUser(request);
        List<Strategy> bondDoubleLowResults = strategyService.queryStrategy(user.getId());
        return ResponseEntity.ok(ResultEntity.ok(bondDoubleLowResults));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/strategy/daily/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryStrategyDaily(@NotBlank(message = "id must not be empty") @PathVariable("id") String id){
        List<PortfolioDaily> portfolioDailies = strategyService.queryPortfolioDaily(id);
        return ResponseEntity.ok(ResultEntity.ok(portfolioDailies));
    }

}
