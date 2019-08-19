package org.naruto.framework.core.exception;


public class ServiceException  extends RuntimeException implements ICommonError {

    private ICommonError commonError;

    public ICommonError getCommonError(){
        return this.commonError;
    }

    public ServiceException(ICommonError commonError){
        super();
        this.commonError = commonError;
    }

    public ServiceException(ICommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public String getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public Object getData() {
        return this.commonError.getData();
    }

    @Override
    public ICommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }


}
