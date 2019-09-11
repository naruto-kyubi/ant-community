package org.naruto.framework.security.service.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.token")
public class JWTTokenConfigProperties {

    //token刷新时间间隔，300s，5分钟一刷新;
    private int refreshInterval = 300;
    //cookies最大存活3天时间。
    private int cookiesMaxAge = 259200;
    //token默认1小时超时；
    private int expire = 3600;

}
