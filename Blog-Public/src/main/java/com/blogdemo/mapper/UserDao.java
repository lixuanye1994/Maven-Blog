package com.blogdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blogdemo.domain.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户表(User)表数据库访问层
 *
 * @author vmlee
 * @since 2022-12-16 10:20:06
 */
@Repository("userDao")
public interface UserDao extends BaseMapper<User> {

}

