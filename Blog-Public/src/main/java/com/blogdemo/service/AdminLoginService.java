package com.blogdemo.service;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;

/**
 * @author VMlee
 */
public interface AdminLoginService {
    /**
     * 后台登录接口
     * @param user
     * @return
     */
    ResponseResult login(User user);

    ResponseResult logout();
}
