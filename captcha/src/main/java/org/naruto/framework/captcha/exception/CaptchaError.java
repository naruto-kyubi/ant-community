package org.naruto.framework.captcha.exception;

import org.naruto.framework.core.exception.CommonError;

public class CaptchaError extends CommonError {
    public static CommonError CAPTCHA_SERVICE_ERROR = new CaptchaError("captcha.unknown.exception","Service Error");
    public static CommonError CAPTCHA_INCORRECT_ERROR = new CaptchaError("captcha.incorrect.exception","incorrect captcha code.");
    public static CommonError CAPTCHA_TIMEOUT_ERROR = new CaptchaError("captcha.timeout.exception","captcha timeout.");

    private CaptchaError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
