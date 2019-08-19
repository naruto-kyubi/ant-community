package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.User;

public interface IAuthenticationService {

    User authenticate(LogonUser logonUser);

    User getCurrentUser();

    void logout(User user);
}
