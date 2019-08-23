package org.naruto.framework.user.exception;

import org.naruto.framework.core.exception.CommonError;

public class UserError extends CommonError {
    public static CommonError USER_EXIST_ERROR = new UserError("user.exist.exception","User aleady exist");
    public static CommonError USER_NOT_EXIST_ERROR = new UserError("user.not.exist.exception","User not exist");
    public static CommonError NICKNAME_EXIST_ERROR = new UserError("nickname.exist.exception","Nickname aleady in use");
    private UserError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
