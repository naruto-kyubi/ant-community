package org.naruto.framework.security.service;


import org.naruto.framework.user.service.IOauthService;
import org.naruto.framework.user.domain.User;

public interface ILogonService {

    User authenticate(LogonUser logonUser);

    IOauthService getOAuthService(String type);

//    ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName);
//    ThirdPartyUser bind(User user, String authType, String authCode);
//    void unbind(User user, String authType);
}
