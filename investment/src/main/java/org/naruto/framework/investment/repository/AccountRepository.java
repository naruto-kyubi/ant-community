package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface AccountRepository extends CustomRepository<Account,String>{

    @Query(value="select * from accounts where owner=?1 and parent=-1 ",
            nativeQuery = true
    )
    public List<Account> queryMainAccountByOwner(String owner);

    @Query(value="select * from accounts where owner=?1 and account_type_id!='-1' and if(?2 !='',parent=?2,1=1) and if(?3 !='',account_type_id=?3,1=1) order by parent",
            nativeQuery = true
    )

    public List<Account> queryAccountsByParentAndType(String owner,String parent,String type);

    public Account queryAccountById(String id);

    List<Account> findAccountsByOwnerAndParentNot(String owner,String parent);

    @Query(value="select sum(balance) from accounts where owner=?1",
            nativeQuery = true
    )
    public Float queryBalanceByOwner(String owner);
}