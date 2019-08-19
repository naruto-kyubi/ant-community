package org.naruto.framework.core.security;

import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;

public interface IOauthService {

    ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName);

    ThirdPartyUser bind(User user, String bindType, String authCode);

    void unbind(User user, String authType);

    OauthUserInfo getOAuthUserInfo(String authCode) ;

}
