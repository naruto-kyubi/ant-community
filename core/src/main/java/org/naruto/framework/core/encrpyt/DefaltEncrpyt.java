package org.naruto.framework.core.encrpyt;

import org.naruto.framework.core.utils.MD5;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name="encrpytService")
public class DefaltEncrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return MD5.encrypt(str, salt);
    }
}
