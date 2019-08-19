package org.naruto.framework.security.exception;

import org.naruto.framework.core.exception.CommonError;

public class SecurityError extends CommonError {
    public static CommonError LOGON_PASSWORD_INCORRECT_ERROR = new SecurityError("logon.password.incorrect.error","incorrect userName or password");
    public static CommonError LOGON_MOBILE_INCORRECT_ERROR = new SecurityError("logon.mobile.incorrect.error","incorrect mobile or captcha code.");

    private SecurityError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
