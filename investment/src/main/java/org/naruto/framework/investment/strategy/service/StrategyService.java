package org.naruto.framework.investment.strategy.service;


import org.naruto.framework.investment.strategy.repository.PortfolioDaily;
import org.naruto.framework.investment.strategy.repository.PortfolioDailyRepository;
import org.naruto.framework.investment.strategy.repository.Strategy;
import org.naruto.framework.investment.strategy.repository.StrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyService {

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private PortfolioDailyRepository portfolioDailyRepository;

    public List<Strategy> queryStrategy(String owner){
        return strategyRepository.findAll();
    }

    public List<PortfolioDaily> queryPortfolioDaily(String id) {
        return portfolioDailyRepository.queryPortfolioDaily(id);
    }
}
