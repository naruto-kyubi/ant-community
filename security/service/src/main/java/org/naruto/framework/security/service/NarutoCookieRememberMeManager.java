package org.naruto.framework.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

@Slf4j
public class NarutoCookieRememberMeManager extends CookieRememberMeManager {

    @Override
    public void onSuccessfulLogin(Subject subject, AuthenticationToken token, AuthenticationInfo info) {

        if (isRememberMe(token)) {
            //always clear any previous identity:
            forgetIdentity(subject);
            //now save the new identity:
            rememberIdentity(subject, token, info);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("AuthenticationToken did not indicate RememberMe is requested.  " +
                        "RememberMe functionality will not be executed for corresponding account.");
            }
        }
    }
}
