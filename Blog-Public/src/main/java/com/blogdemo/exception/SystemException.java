package com.blogdemo.exception;

import com.blogdemo.domain.enums.AppHttpCodeEnum;

/**
 *   统一异常处理中心
 *   之后有需要抛出异常的代码中，传入SystemException(枚举定义的错误代码)
 */
public class SystemException extends RuntimeException{
    private int code;
    private String msg;
    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }


}
