package org.naruto.framework.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public static String encrypt(String text, String key) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + key);
//        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }
}
