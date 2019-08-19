package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.User;

import javax.servlet.http.HttpServletRequest;

public interface ISessionService {

    User getCurrentUser(HttpServletRequest request);
    void logout(User user);

}
