package org.naruto.framework.core.utils;


import org.springframework.util.DigestUtils;

public class MD5 {
    public static String encrypt(String text, String key) {

        //加密后的字符串
        String encodeStr=DigestUtils.md5DigestAsHex((text + key).getBytes());
        return encodeStr;
    }
}
