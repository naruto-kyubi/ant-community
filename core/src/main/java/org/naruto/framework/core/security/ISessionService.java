package org.naruto.framework.core.security;

import org.naruto.framework.core.user.User;

import javax.servlet.http.HttpServletRequest;

public interface ISessionService {

    User getCurrentUser(HttpServletRequest request);
    void logout(User user);

}
