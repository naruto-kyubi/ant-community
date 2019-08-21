package org.naruto.framework.core.security;

import org.naruto.framework.core.user.ThirdPartyUser;
import org.naruto.framework.core.user.User;

public interface ILogonService {

    User authenticate(LogonUser logonUser);

    ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName);

    ThirdPartyUser bind(User user, String authType, String authCode);

    void unbind(User user, String authType);
}
