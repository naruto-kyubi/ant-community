package org.naruto.framework.security.service.weibo;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.service.ThirdPartyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeiboAuthorizingRealm extends AuthenticatingRealm {

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        WeiboToken weiboToken = (WeiboToken) token;

        ThirdPartyUser thirdPartyUser = thirdPartyUserService.queryThirdPartyUserByAuthTypeAndUid(weiboToken.getType(),weiboToken.getUid());
        if (null == thirdPartyUser) {
            throw new AuthenticationException(" weibo not binding with a main account");
        }
        return new SimpleAuthenticationInfo(thirdPartyUser.getUser(), thirdPartyUser.getUid(), this.getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WeiboToken;
    }
}
