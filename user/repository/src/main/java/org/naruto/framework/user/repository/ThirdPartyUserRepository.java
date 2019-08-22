package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ThirdPartyUserRepository extends CustomRepository<ThirdPartyUser,String> {

    ThirdPartyUser queryThirdPartyUserByAuthTypeAndUid(String type, String uid);

    List<ThirdPartyUser> queryThirdPartyUsersByUser(User user);

    @Transactional
    void deleteThirdPartyUsersByUserAndAuthType(User user, String authType);
}
