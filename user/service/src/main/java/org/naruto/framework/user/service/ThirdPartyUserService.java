package org.naruto.framework.user.service;

import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.security.IOauthService;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.ThirdPartyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ThirdPartyUserService {

    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;

    @Autowired
    private Map<String, IOauthService> oAuthServiceMap = new ConcurrentHashMap<>();

    public ThirdPartyUser save(ThirdPartyUser thirdPartyUser){
        return thirdPartyUserRepository.save(thirdPartyUser);
    }

    public ThirdPartyUser queryThirdPartyUserByAuthTypeAndUid(String type, String uid){
        return thirdPartyUserRepository.queryThirdPartyUserByAuthTypeAndUid(type,uid);
    }

    public List<ThirdPartyUser> queryThirdPartyUsersByUser(User user){
        return  thirdPartyUserRepository.queryThirdPartyUsersByUser(user);
    }

    @Transactional
    public void unbind(User user, String authType){
        thirdPartyUserRepository.deleteThirdPartyUsersByUserAndAuthType(user,authType);
    }


    public ThirdPartyUser bind(User user , String bindType, String bindUid, String bindName){

        return (ThirdPartyUser)getOAuthService(bindType).bind(user,bindType,bindUid,bindName);
    }

    public ThirdPartyUser bind(User user , String authType, String authCode){
        return (ThirdPartyUser) getOAuthService(authType).bind(user,authType,authCode);
    }

//    public void unbind(User user , String authType){
//        getOAuthService(authType).unbind(user,authType);
//    }

//
    private IOauthService getOAuthService(String type){

     //   if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        IOauthService oauthService = oAuthServiceMap.get(type.concat("OauthService"));
     //   if(null==oauthService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);
        return oauthService;
    }


}
