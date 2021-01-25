package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountDailyReportRepository extends CustomRepository<AccountDailyReport,String> {

    @Query(value="select * from accounts_daily_report where date(statistic_at) = curdate() and owner=?1",
            nativeQuery = true
    )
    public List<AccountDailyReport> queryAccountDailyReportByDate(String owner);
}
