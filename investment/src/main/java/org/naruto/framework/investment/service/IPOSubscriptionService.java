package org.naruto.framework.investment.service;

import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.IPOSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Scope("prototype")
@Service
public class IPOSubscriptionService {

    @Autowired
    private IPOSubscriptionRepository ipoSubscriptionRepository;

    public List<IPOSubscription> findIPOSubscriptions(String stockCode, String nameCn, String type){

        return ipoSubscriptionRepository.findIPOSubscriptions(stockCode,nameCn,type);
    }
}
