package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blogdemo.domain.entity.LoginUser;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.exception.SystemException;
import com.blogdemo.mapper.UserDao;
import com.blogdemo.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author VMlee
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userDao.selectOne(queryWrapper);
        // 判断是否查到用户，如果没有用户抛出异常
        if(Objects.isNull(user)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOEXIST);
            //throw new RuntimeException("用户名不存在");
        }

        // 返回用户信息
        //TODO 查询权限信息封装
        return new LoginUser(user);
    }
}
