package com.blogdemo.handler.security;

import com.alibaba.fastjson.JSON;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 自定义授权异常的返回情况
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //打印异常信息 AuthenticationException e
        e.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);

        // 响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
