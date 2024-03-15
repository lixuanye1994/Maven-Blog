package com.blogdemo.service;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;

/**
 * @author VMlee
 */
public interface UserLoginService {
    /**
     * 用户登录接口
     * @param user
     * @return
     */
    ResponseResult login(User user);

    ResponseResult logout();
}
