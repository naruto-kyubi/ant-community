package org.naruto.framework.core.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naruto.framework.core.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity<T> {

//    ok,fail;
    private String status;

    //errCode,errMsg;
    private Object error;

    private Object data;

    private Object meta;

    public static ResultEntity ok(Object data){
        return new ResultEntity("ok",null,data,null);
    }

    public static ResultEntity ok(Object data, Object meta){
        return new ResultEntity("ok",null,data,meta);
    }

    public static ResultEntity fail(ServiceException serviceException){
        Map<String,Object> err = new HashMap<String,Object>();

        err.put("errCode",serviceException.getErrCode());
        err.put("errMsg",serviceException.getErrMsg());

        return new ResultEntity("fail",err,serviceException.getData(),null);
    }

//    public static ResultEntity fail(ServiceException serviceException,Object objData){
//        Map<String,Object> err = new HashMap<String,Object>();
//
//        err.put("errCode",serviceException.getErrCode());
//        err.put("errMsg",serviceException.getErrMsg());
//
//        return new ResultEntity("fail",err,objData,null);
//    }
}
