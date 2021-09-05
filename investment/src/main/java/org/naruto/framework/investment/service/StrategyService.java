package org.naruto.framework.investment.service;


import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.BondDoubleLowResult;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.repository.StockRepository;
import org.naruto.framework.investment.repository.StrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Scope("prototype")
@Service
public class StrategyService {
    @Autowired
    private StrategyRepository strategyRepository;

    public List<BondDoubleLowResult> findAll() {
        return strategyRepository.findAll();
    }
}
