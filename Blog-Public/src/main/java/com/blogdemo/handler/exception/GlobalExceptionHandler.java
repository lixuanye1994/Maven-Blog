package com.blogdemo.handler.exception;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  全局统一处理异常，@ExceptionHandler(SystemException.class)
 *  从自定义的SystemException中获得异常抛出的code 和 msg，封装成日志格式，并返回定义的错误信息
 */
@RestControllerAdvice
// Slf4j 打印日志,lombok提供的接口,有注解后才可以使用log.error()
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理举例，捕获用户名密码错误等
     * @param e
     * @return 例如{code:505,msg:用户名或密码错误}
     */
    @ExceptionHandler({SystemException.class,BadCredentialsException.class,InsufficientAuthenticationException.class})
    public ResponseResult systemExceptionHandler(SystemException e){
        // 打印异常信息
        log.error("出现异常！ {}",e);
        // 从异常对象中获取提示信息封装返回，响应给前端
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    // 处理意料之外的异常
    // TODO 目前已经进入LoginController()的错误都是exception接管统一错误代码500，没有到上面的SystemException无法做到自定义错误代码
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception  e){
        // 打印异常信息
        log.error("出现其他意料之外的异常！{}",e);
        // 从枚举中自定义错误500代码,从Exception抛出的错误中给到信息
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }

}
