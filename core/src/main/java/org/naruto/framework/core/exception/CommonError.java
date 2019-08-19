package org.naruto.framework.core.exception;

public class CommonError implements ICommonError{
    public static CommonError PARAMETER_VALIDATION_ERROR = new CommonError("sys.invalid-parameter.exception","Invalid Parameter");
    public static CommonError UNKNOWN_ERROR = new CommonError("sys.unkown.exception","unknow exception");

    private String errCode;
    private String errMsg;

    private Object data;

    @Override
    public  String getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }


    public CommonError(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
