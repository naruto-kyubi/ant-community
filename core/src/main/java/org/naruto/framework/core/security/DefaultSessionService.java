package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@ConditionalOnMissingBean(name="sessionService")
public class DefaultSessionService implements ISessionService {
    @Override
    public User getCurrentUser(HttpServletRequest request) {

        return null;
    }

    @Override
    public void logout(User user) {

    }
}
