package org.naruto.framework.investment.repository.etl;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class Text2HttpConverter extends MappingJackson2HttpMessageConverter {
    public Text2HttpConverter(){
        List<MediaType> mediaTypeList= new ArrayList<>();
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypeList);
    }
}
