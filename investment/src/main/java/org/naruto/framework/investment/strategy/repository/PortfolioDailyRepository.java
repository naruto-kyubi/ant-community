package org.naruto.framework.investment.strategy.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioDailyRepository extends CustomRepository<PortfolioDaily,String> {
    // 查询多个组合的日收益，可以是股票，基金，某一策略形成的组合，
    // 按照时间顺序返回结果，
    @Query(value="select * from portfolio_daily  where portfolio_id = ?1 or ts_code='510300.SH'",
            nativeQuery = true
    )
    public List<PortfolioDaily> queryPortfolioDaily(String portfolioId);

}
