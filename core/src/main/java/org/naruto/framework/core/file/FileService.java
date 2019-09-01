package org.naruto.framework.core.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {

    //读取配置文件中的文件存储路径；
    @Value("${img.location}")
    private String location;

    @Value("${spring.mvc.static-path-pattern}")
    private String staticPathPattern;


    public String uploadFile(MultipartFile file) throws Exception {
        String contentType = file.getContentType();

        String orginalFileName = file.getOriginalFilename();
        String fileName = StringUtils.substringBefore(orginalFileName,".").concat(UUID.randomUUID().toString()).concat(orginalFileName.substring(orginalFileName.lastIndexOf(".")));

        File targetFile = new File(location);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(location.concat("/").concat(fileName));
        out.write(file.getBytes());
        out.flush();
        out.close();
        return StringUtils.substringBefore(staticPathPattern,"*").concat(fileName);
    }
}
