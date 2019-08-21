package org.naruto.framework.core.security;

import org.naruto.framework.core.user.ThirdPartyUser;
import org.naruto.framework.core.user.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name="logonService")
public class DefaultLogonService implements ILogonService {
    @Override
    public User authenticate(LogonUser logonUser) {
        return null;
    }

//    @Override
//    public ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName) {
//        return null;
//    }
//
//    @Override
//    public ThirdPartyUser bind(User user, String authType, String authCode) {
//        return null;
//    }
//
//    @Override
//    public void unbind(User user, String authType) {
//
//    }
}
