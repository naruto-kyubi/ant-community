package org.naruto.framework.security.exception;

import org.naruto.framework.core.exception.CommonError;

public class SecurityError extends CommonError {
    public static CommonError LOGON_PASSWORD_INCORRECT_ERROR = new SecurityError("logon.password.incorrect.error","incorrect userName or password");
    public static CommonError LOGON_MOBILE_INCORRECT_ERROR = new SecurityError("logon.mobile.incorrect.error","incorrect mobile or captcha code.");

    public static CommonError USER_HAS_BEEN_LOGOUT = new SecurityError("user.has.been.logout.error","user has been logout.");
    public static CommonError EXCEED_AUTHORITY_ERROR = new SecurityError("no.authority.error","no authority error.");

    private SecurityError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
