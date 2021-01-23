package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPOSubscriptionRepository extends CustomRepository<IPOSubscription,String> {

    @Query(value="select ipo from IPOSubscription as ipo where ipo.stock.id=?1")
    List<IPOSubscription> findIPOSubscriptions(String stockId);
}
