package org.naruto.framework.investment.repository.etl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExtractBasic implements Extract{
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    LoadingManager loadingManager;

    void setUp(){
        restTemplate.getMessageConverters().add(new Text2HttpConverter());
    }

    @Override
    public void extractList(ExtractRequest extractRequest) {

    }

    @Override
    public void extractSingleObject(ExtractRequest extractRequest) {

    }
}
