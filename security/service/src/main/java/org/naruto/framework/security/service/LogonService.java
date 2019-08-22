package org.naruto.framework.security.service;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.user.service.IOauthService;
import org.naruto.framework.security.exception.SecurityError;
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

    private IAuthenticationService getAuthenticationService(String type){

        if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        IAuthenticationService authenticationService = authenticationServiceMap.get(type.concat("AuthenticationService"));
        if(null==authenticationService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        return authenticationService;
    }
//
    public IOauthService getOAuthService(String type){

        if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        IOauthService oauthService = oAuthServiceMap.get(type.concat("OauthService"));
        if(null==oauthService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        return oauthService;
    }
}
