package org.naruto.framework.core.encrpyt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component("encrpytService")
@ConditionalOnProperty(name = "naruto.encrpyt",havingValue = "md5")
public class MD5Encrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return "md5";
    }
}
