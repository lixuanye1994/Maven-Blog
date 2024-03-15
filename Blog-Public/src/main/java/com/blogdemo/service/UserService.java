package com.blogdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author vmlee
 * @since 2023-02-08 17:11:59
 */
public interface UserService extends IService<User> {

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}

