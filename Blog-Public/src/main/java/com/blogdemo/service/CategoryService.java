package com.blogdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-11-30 15:20:51
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();
}

