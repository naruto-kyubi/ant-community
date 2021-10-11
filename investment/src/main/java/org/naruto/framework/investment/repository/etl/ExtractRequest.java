package org.naruto.framework.investment.repository.etl;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExtractRequest {
    private String apiName;
    private String resourceUrl;
    private String token;
    private Map<String,Object> params = new HashMap<String, Object>();

}
