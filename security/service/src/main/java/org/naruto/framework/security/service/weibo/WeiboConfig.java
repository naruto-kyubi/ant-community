package org.naruto.framework.security.service.weibo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weibo")
@Data
public class WeiboConfig {
    private String tokenUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String userUrl;
    private String redirectUri;
}
