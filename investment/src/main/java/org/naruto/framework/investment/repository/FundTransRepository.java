package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FundTransRepository extends CustomRepository<FundTrans,String>{

    @Query(value="select * from fund_trans where status < 4  order by trans_at", nativeQuery = true
    )

    public List<FundTrans> queryFundTransByParentAndType(String owner, String parent, String type);

}