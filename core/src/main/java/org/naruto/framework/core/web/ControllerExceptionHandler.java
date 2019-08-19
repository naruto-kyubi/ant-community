package org.naruto.framework.core.web;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        StringBuffer errorMsgs = new StringBuffer();
        ex.getBindingResult().getAllErrors().forEach(fieldError -> {
            errorMsgs.append(fieldError.getDefaultMessage() + ";");
        });

        ServiceException serviceException = new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        serviceException.setErrMsg(errorMsgs.toString());
        return ResultEntity.fail(serviceException);
    }

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResultEntity serviceExceptionHandler(ServiceException ex) {
        log.error(ex.toString());
        return ResultEntity.fail(ex);
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultEntity exceptionHandler(Exception ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
        return ResultEntity.fail(new ServiceException(CommonError.UNKNOWN_ERROR));
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultEntity missParameterExceptionHandler(MissingServletRequestParameterException ex) {
        ServiceException serviceException = new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        serviceException.setErrMsg(ex.getMessage());
        return ResultEntity.fail(serviceException);
    }
}
