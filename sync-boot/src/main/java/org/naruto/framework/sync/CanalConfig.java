package org.naruto.framework.sync;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "canal.sync")
@Data
public class CanalConfig {
    private String address;
    private int port;
    private String destination;
    private String username;
    private String password;
    private String database;
}
