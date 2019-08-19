package org.naruto.framework.security.service.account;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.security.IAuthenticationService;
import org.naruto.framework.core.security.LogonUser;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.user.domain.User;
import org.springframework.stereotype.Component;

@Component("accountAuthenticationService")
@Slf4j
public class AccountAuthenticationService implements IAuthenticationService {
    @Override
    public User authenticate(LogonUser logonUser) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(logonUser.getUserName(),logonUser.getPassword());
        token.setRememberMe(logonUser.isRememberMe());

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new ServiceException(SecurityError.LOGON_PASSWORD_INCORRECT_ERROR);
        }
        return (User) subject.getPrincipal();
    }

    @Override
    public User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    @Override
    public void logout(User user) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();;
    }
}
