package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SessionUtils {

    @Autowired
    private ISessionService sessionService;

    public User getCurrentUser(HttpServletRequest request){
        return sessionService.getCurrentUser(request);
    }

    public void logout(User user){
        sessionService.logout(user);
        //
    }
}
