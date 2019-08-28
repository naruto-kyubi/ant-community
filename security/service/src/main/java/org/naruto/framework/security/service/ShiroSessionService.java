package org.naruto.framework.security.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.session.ISessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于shiro实现的session管理，实现core中的ISessionService接口。
 */
@Component
@ConditionalOnMissingBean(name="sessionService")
@Primary
public class ShiroSessionService implements ISessionService {

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public Object getCurrentUser(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    /**
     * shiro用户登出
     * @param user
     */
    @Override
    public void logout(Object user) {
        Subject subject = SecurityUtils.getSubject();
        if(null!=subject) subject.logout();
    }
}
