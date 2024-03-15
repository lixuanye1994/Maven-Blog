package com.blogdemo.controller;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.exception.SystemException;
import com.blogdemo.service.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author VMlee
 * 用户登录
 */
@RestController
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @PostMapping("/user/login")
    /**
     * 用户登录，获取用户参数，用User类型接收
     * 传入userLoginService去处理登录验证
     */
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            // 提示必须传入用户名，抛出异常
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return  adminLoginService.logout();
    }
}
