package org.naruto.framework.security.service.weibo;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:weibo.properties")
@Data
public class WeiboConfig {
    private String tokenUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String userUrl;
    private String redirectUri;
}
