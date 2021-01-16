package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AccountRepository extends CustomRepository<Account,String>{

    @Query(value="select * from accounts where owner=?1 and parent=-1 ",
            nativeQuery = true
    )
    public List<Account> queryMainAccountByOwner(String owner);

    @Query(value="select * from accounts where owner=?1 and type!='-1' and if(?2 !='',parent=?2,1=1) and if(?3 !='',type=?3,1=1) order by parent",
            nativeQuery = true
    )
    public List<Account> queryAccountsByParentAndType(String owner,String parent,String type);
    public Account queryAccountById(String id);

}