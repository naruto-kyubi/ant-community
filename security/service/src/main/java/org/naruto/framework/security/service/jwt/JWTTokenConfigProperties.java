package org.naruto.framework.security.service.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.token")
public class JWTTokenConfigProperties {

    private int refreshInterval = 300;
    private int cookiesMaxAge = 259200;
    private int expire = 3600;

}
