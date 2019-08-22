package org.naruto.framework.core.session;

import javax.servlet.http.HttpServletRequest;

public interface ISessionService {

    Object getCurrentUser(HttpServletRequest request);

    void logout(Object user);
}
