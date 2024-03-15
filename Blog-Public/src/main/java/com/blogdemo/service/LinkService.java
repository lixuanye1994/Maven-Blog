package com.blogdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author vmlee
 * @since 2022-12-13 11:48:54
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

