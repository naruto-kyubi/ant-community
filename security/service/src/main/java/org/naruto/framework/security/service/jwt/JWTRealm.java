package org.naruto.framework.security.service.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.naruto.framework.security.domain.Role;
import org.naruto.framework.security.service.UserRoleService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    public JWTRealm(){
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authcToken;
        String token = jwtToken.getToken();

        User user =userService.getUserById(JwtUtils.getUsername(token));

        if(user == null)
            throw new AuthenticationException("token expired，please relogin.");

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPasswordSalt(),this.getName());
        return authenticationInfo;
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
//        Set<Role> roles = userService.getUserRoles(user.getId());

        //获取集合某一对象属性集合；
        List<String> roleList = (List<String>) CollectionUtils.collect(userRoleService.queryRolesByUserId(user.getId()), new Transformer() {
            @Override
            public Object transform(Object o) {
                Role role = (Role) o;
                return role.getId();
            }
        });

        if (roleList != null) simpleAuthorizationInfo.addRoles(roleList);
        return simpleAuthorizationInfo;
    }
}
