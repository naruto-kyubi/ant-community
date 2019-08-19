package org.naruto.framework.security.service;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.security.IAuthenticationService;
import org.naruto.framework.core.security.ILogonService;
import org.naruto.framework.core.security.IOauthService;
import org.naruto.framework.core.security.LogonUser;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class LogonService implements ILogonService {
    @Autowired
    private Map<String, IAuthenticationService> authenticationServiceMap = new ConcurrentHashMap<>();

    @Autowired
    private Map<String, IOauthService> oAuthServiceMap = new ConcurrentHashMap<>();

    public User authenticate(LogonUser logonUser){
        return getAuthenticationService(logonUser.getAuthType()).authenticate(logonUser);
    }

    public ThirdPartyUser bind(User user , String bindType, String bindUid, String bindName){

        return getOAuthService(bindType).bind(user,bindType,bindUid,bindName);
    }

    public ThirdPartyUser bind(User user , String authType, String authCode){
        return getOAuthService(authType).bind(user,authType,authCode);
    }

    public void unbind(User user , String authType){
        getOAuthService(authType).unbind(user,authType);
    }

    private IAuthenticationService getAuthenticationService(String type){

        if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        IAuthenticationService authenticationService = authenticationServiceMap.get(type.concat("AuthenticationService"));
        if(null==authenticationService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        return authenticationService;
    }

    private IOauthService getOAuthService(String type){

        if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        IOauthService oauthService = oAuthServiceMap.get(type.concat("OauthService"));
        if(null==oauthService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        return oauthService;
    }
}
