package org.naruto.framework.investment.service;

import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.IPOSubscriptionRepository;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Scope("prototype")
@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }
//
//    public List<IPOSubscription> findIPOSubscriptions(String stockCode, String nameCn, String type){
//
//        return ipoSubscriptionRepository.findIPOSubscriptions(stockCode,nameCn,type);
//    }
}
