package org.naruto.framework.security.service;


import org.naruto.framework.user.service.IOauthService;
import org.naruto.framework.user.domain.User;

public interface ILogonService {

    User authenticate(LogonUser logonUser);

    IOauthService getOAuthService(String type);
}
