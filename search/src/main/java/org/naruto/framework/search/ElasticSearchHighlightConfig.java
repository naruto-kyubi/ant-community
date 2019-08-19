package org.naruto.framework.search;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elasticsearch.highlight")
@Data
public class ElasticSearchHighlightConfig {
    private String preTag;
    private String postTag;
}
