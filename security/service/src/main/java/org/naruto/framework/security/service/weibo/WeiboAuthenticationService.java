package org.naruto.framework.security.service.weibo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.security.IAuthenticationService;
import org.naruto.framework.core.security.IOauthService;
import org.naruto.framework.core.security.LogonUser;
import org.naruto.framework.core.security.OauthUserInfo;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("weiboAuthenticationService")
@Slf4j
public class WeiboAuthenticationService implements IAuthenticationService {

    @Autowired
    private IOauthService weiboOauthService;

    @Override
    public User authenticate(LogonUser logonUser) {

        OauthUserInfo oauthUserInfo = null;
        Subject subject = SecurityUtils.getSubject();
        try {
            oauthUserInfo = weiboOauthService.getOAuthUserInfo(logonUser.getAuthCode());
            WeiboToken token = new WeiboToken(logonUser.getAuthType(),oauthUserInfo.getUid(),logonUser.isRememberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            WeiboError error = WeiboError.WEIBO_NOTBINDED_ERROR;
            error.setData(oauthUserInfo);
            throw new ServiceException(error);
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
        subject.logout();
    }
}
