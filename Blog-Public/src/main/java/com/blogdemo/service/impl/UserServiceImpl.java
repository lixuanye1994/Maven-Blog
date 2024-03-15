package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.domain.vo.UserinfoVO;
import com.blogdemo.exception.SystemException;
import com.blogdemo.mapper.UserDao;
import com.blogdemo.domain.entity.User;
import com.blogdemo.service.UserService;
import com.blogdemo.utils.BeanCopyUtilsVO;
import com.blogdemo.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



/**
 * 用户表(User)表服务实现类
 *
 * @author vmlee
 * @since 2023-02-08 17:11:59
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {


    @Override
    public ResponseResult getUserInfo() {
        //1.从缓存中获取用户
        User user = SecurityUtils.getLoginUser().getUser();
        //2.vo类筛选返回
        UserinfoVO vo = BeanCopyUtilsVO.copyBean(user, UserinfoVO.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        // 省略判断,减少了一些其他操作，可根据自己要求改造
        updateById(user);
        return ResponseResult.okResult();
    }

    // SecurityConfig.java中已经注入了PasswordEncoder 加密算法，这里直接拿来用
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册传入值用User类接收,此处代码很丑，后期改成validation参数校验
     * @param user
     * 实际有 username password nickname ，email
     * @return 200
     */
    @Override
    public ResponseResult register(User user) {
        // 对新注册账户数据进行非空判断  null 和 空字符串 “ ” 不能作为用户名
        // 用StringUtils.hasText()判断是否有文本，推荐
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_NICKNAME);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_EMAIL);
        }
        // 对新注册账户数据进行是否已存在判断,暂时只判断账号
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 确认可以保存数据库
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 数据库查询当前传入账号是否存在，如果存在count值大于1，返回真
     * @param userName
     * @return boolean
     */
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getUserName,userName);
        return count(query)>0;
    }
}

