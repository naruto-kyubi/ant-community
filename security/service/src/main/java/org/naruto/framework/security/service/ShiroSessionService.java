package org.naruto.framework.security.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.session.ISessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@ConditionalOnMissingBean(name="sessionService")
@Primary
public class ShiroSessionService implements ISessionService {

    @Override
    public Object getCurrentUser(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    @Override
    public void logout(Object user) {
        Subject subject = SecurityUtils.getSubject();
        if(null!=subject) subject.logout();
    }
}
