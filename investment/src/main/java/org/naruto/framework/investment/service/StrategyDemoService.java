package org.naruto.framework.investment.service;


import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Scope("prototype")
@Service
public class StrategyDemoService {
    @Autowired
    private StockValuationDashborardRepository stockValuationDashborardRepository;


    public List<StockValuationDashborard> queryStockValuationDashborard(){
        return stockValuationDashborardRepository.findAll();
    }
}
