package com.blogdemo.filter;

import com.alibaba.fastjson.JSON;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.LoginUser;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.utils.JwtUtil;
import com.blogdemo.utils.RedisCache;
import com.blogdemo.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 登录校验过滤器 - jwt认证过滤器
 * 获取token
 * 解析token 获取userid
 * 从redis中获取用户信息
 * 存入SecurityContextHolder
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取 请求头 中的 token
        String token = httpServletRequest.getHeader("token");
        if(!StringUtils.hasText(token)){
            // 如果传入数据没有token内容,说明该接口不需要登陆,放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        // 解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * token超时或者非法的token
             * 需要告诉前端重新登录(使用WebUtils工具类处理数据)
             */
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            // WebUtils工具类 将数据响应给前端并渲染成Json格式
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }

        String userId = claims.getSubject();
        // 从redis中获取用户信息 "bloglogin"+userId,loginUser
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);

        if(Objects.isNull(loginUser)){
            // 说明登录过期  提示重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }

        // 登录成功把LoginUser信息封装存入SecurityContextHolder,每一个线程只存放自己的数据,多用户使用时只访问自己的线程数据
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
