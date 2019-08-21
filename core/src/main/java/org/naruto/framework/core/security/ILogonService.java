package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;

public interface ILogonService {

    User authenticate(LogonUser logonUser);
    ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName);
    ThirdPartyUser bind(User user, String authType, String authCode);
    void unbind(User user, String authType);
}
