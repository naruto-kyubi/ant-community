package org.naruto.framework.security.service.weibo;

import org.naruto.framework.core.exception.CommonError;

public class WeiboError extends CommonError {

    public static WeiboError WEIBO_NOTBINDED_ERROR = new WeiboError("user.weibo.notbinded.exception","微博未绑定");

    private WeiboError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
