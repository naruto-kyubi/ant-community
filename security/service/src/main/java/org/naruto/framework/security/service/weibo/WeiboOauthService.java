package org.naruto.framework.security.service.weibo;

import com.alibaba.fastjson.JSONObject;
import org.naruto.framework.user.service.IOauthService;
import org.naruto.framework.user.service.OauthUserInfo;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.ThirdPartyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class WeiboOauthService implements IOauthService{

    @Autowired
    private WeiboConfig weiboConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

    /**
     * 用户与微博账户绑定
     * @param user
     * @param bindType
     * @param bindUid
     * @param bindName
     * @return
     */
    @Override
    public ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName) {
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser(null,bindType,bindUid,bindName,(org.naruto.framework.user.domain.User)user);
        return thirdPartyUserService.save(thirdPartyUser);
    }

    /**
     * user与微博账号绑定
     * @param user
     * @param bindType
     * @param authCode
     * @return
     */
    @Override
    public ThirdPartyUser bind(User user, String bindType, String authCode) {
        OauthUserInfo userInfo = this.getOAuthUserInfo(authCode);
        return this.bind(user,bindType,userInfo.getUid(),userInfo.getName());
    }

    /**
     * 解除用户与微博账号的绑定
     * @param user
     * @param authType
     */
    @Override
    public void unbind(User user, String authType) {

        thirdPartyUserService.unbind(user,authType);
    }

    /**
     * 访问微博服务，获取微博账户信息。
     * @param authCode
     * @return
     */
    @Override
    public OauthUserInfo getOAuthUserInfo(String authCode) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("code", authCode);
        map.add("client_id", weiboConfig.getClientId());
        map.add("client_secret", weiboConfig.getClientSecret());
        map.add("grant_type", weiboConfig.getGrantType());
        map.add("redirect_uri", weiboConfig.getRedirectUri());

        JSONObject objToken = restTemplate.postForObject(weiboConfig.getTokenUrl(), map, JSONObject.class);
        String access_token = objToken.getString("access_token");
        String uid = objToken.getString("uid");

        Map<String, String> userMap = new HashMap();
        userMap.put("access_token", access_token);
        userMap.put("uid", uid);
        String userUrl = weiboConfig.getUserUrl().concat("?access_token={access_token}&uid={uid}");
        JSONObject objUser = restTemplate.getForObject(userUrl,JSONObject.class,userMap);
        String name = objUser.getString("name");
        return new OauthUserInfo(uid,name,null);
    }
}
