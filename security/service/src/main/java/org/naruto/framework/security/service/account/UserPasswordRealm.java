package org.naruto.framework.security.service.account;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordRealm extends AuthenticatingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    public UserPasswordRealm(PasswordCredentialsMatcher passwordCredentialsMatcher){
        this.setCredentialsMatcher(passwordCredentialsMatcher);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
        String username = userpasswordToken.getUsername();
        User user = userService.getUserByMobile(username);
        if(user == null)
            throw new AuthenticationException("Invalid userName or password");

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getPasswordSalt()), this.getName());
    }
}
