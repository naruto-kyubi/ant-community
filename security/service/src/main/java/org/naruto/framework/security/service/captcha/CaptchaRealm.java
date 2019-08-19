package org.naruto.framework.security.service.captcha;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaRealm extends AuthenticatingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        CaptchaToken captchaToken = (CaptchaToken)token;
        User user = userService.getUserByMobile(captchaToken.getMobile());
        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        Captcha captcha = captchaService.getCaptcha(captchaToken.getMobile(),CaptchaType.LOGON);
        return new SimpleAuthenticationInfo(user, captcha.getCaptcha(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CaptchaToken;
    }
}
