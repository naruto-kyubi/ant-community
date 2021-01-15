package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPOSubscriptionRepository extends CustomRepository<IPOSubscription,String> {

    @Query(value="select ipo from IPOSubscription as ipo where ipo.stock.code=?1 and ipo.account.nameCn like ?2 and ipo.account.type like ?3")
    List<IPOSubscription> findIPOSubscriptions(String stockCode,String nameCn,String type);
}
