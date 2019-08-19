package org.naruto.framework.core.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {

    @Value("${uploadfile.tmp.location}")
    private String uploadfilelocation;

    //设置限制文件大小。
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("10240KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");

        //文件上传临时目录
        factory.setLocation(uploadfilelocation);
        return factory.createMultipartConfig();
    }

}
