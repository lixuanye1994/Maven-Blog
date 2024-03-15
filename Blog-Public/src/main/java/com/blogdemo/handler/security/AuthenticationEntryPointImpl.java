package com.blogdemo.handler.security;

import com.alibaba.fastjson.JSON;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.exception.SystemException;
import com.blogdemo.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证异常的返回情况
 * 拦截错误名称BadCredentialsException：定义为 登录时用户名密码错误异常，提示前端用户名密码错误
 * 拦截错误名称InsufficientAuthenticationException：定义为 没有token时没有权限操作，提示前端需要登陆
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        // 打印异常信息 AuthenticationException e,用于查看当前异常错误的种类
        e.printStackTrace();
        ResponseResult r = null;

        // 登录时用户名密码错误异常：BadCredentialsException
        if(e instanceof BadCredentialsException){

            // 用自定义的错误代码505,加上系统出错给出的message,来精准给出错误信息
            r = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());

        }else if(e instanceof InsufficientAuthenticationException){
            // 没有token时没有权限操作：InsufficientAuthenticationException

            // 此处错误异常纯英文,用自定义错误更直观
            r = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);

        }else{

            // 仍有其他异常错误未知,统一归纳到此处
            r = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或者授权失败");
        }
        // 响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(r));

    }
}
