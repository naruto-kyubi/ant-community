package org.naruto.framework.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "captcha.aliyuncs")
@Data
public class CaptchaConfig {
    private String regionId;
    private String domain;
    private String version;
    private String accessKey;
    private String accessSecret;
    private String signName;
    private String templateCode;
    private Integer expiryDate;
}
