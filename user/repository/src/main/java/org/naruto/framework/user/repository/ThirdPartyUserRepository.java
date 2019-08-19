package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;

import java.util.List;


public interface ThirdPartyUserRepository extends CustomRepository<ThirdPartyUser,String> {

    public ThirdPartyUser queryThirdPartyUserByAuthTypeAndUid(String type, String uid);

    public List<ThirdPartyUser> queryThirdPartyUsersByUser(User user);

    public void deleteThirdPartyUsersByUserAndAuthType(User user, String authType);
}
