package com.blogdemo.service.impl;


import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.LoginUser;
import com.blogdemo.domain.entity.User;
import com.blogdemo.domain.vo.UserLoginVO;
import com.blogdemo.domain.vo.UserinfoVO;
import com.blogdemo.service.UserLoginService;
import com.blogdemo.utils.BeanCopyUtilsVO;
import com.blogdemo.utils.JwtUtil;
import com.blogdemo.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author VMlee
 * 用户登录服务实现类
 */
@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    /*
      AuthenticationManager（接口）是认证相关的核心接口
      Spring Security 内容知识
     */
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 安全性校验,判断认证是否通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("未通过AuthenticationManager校验:用户名或密码错误");
        }
        // 获取Userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // 把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        LoginUser loginUser1 = redisCache.getCacheObject("bloglogin:"+userId);
        System.out.println("当前ID用户登录咯:"+loginUser1.getUsername());

        /*
         * 把token和userinfo封装 返回
         * 把User转换成UserinfoVO
          */
        UserinfoVO userInfo = BeanCopyUtilsVO.copyBean(loginUser.getUser(), UserinfoVO.class);
        UserLoginVO vo = new UserLoginVO(jwt,userInfo);
        return ResponseResult.okResult(vo);
    }

    /**
     * 退出登录
     *
     */
    @Override
    public ResponseResult logout() {
        // TODO 退出登录时,需要同时在redis中删除token
        // 之前登录时把LoginUser封装存入SecurityContextHolder中
        // 从SecurityContextHolder 中获取token 解析获取userid

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取LoginUser属性
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户id
        Long userId = loginUser.getUser().getId();

        // 获取用户登录名
        String username = loginUser.getUser().getUserName();
        System.out.println("当前ID用户退出咯,期待下次光临:"+username);

        // 删除redis中的用户信息
        redisCache.deleteObject("bloglogin:" + userId);

        return  ResponseResult.okResult();

    }
}
