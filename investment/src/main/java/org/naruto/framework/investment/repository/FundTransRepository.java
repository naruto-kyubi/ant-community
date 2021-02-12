package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FundTransRepository extends CustomRepository<FundTrans,String>{

    @Query(value="select * from fund_trans f,accounts t where f.account = t.id and f.status < 4 and t.owner=?1  and if(?2 !='',t.parent=?2,1=1) and if(?3 !='',t.account_type_id=?3,1=1) order by f.trans_at", nativeQuery = true
    )

    public List<FundTrans> queryFundTransByParentAndType(String owner, String parent, String type);

}