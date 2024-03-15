package com.blogdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Article;

/**
 * 文章表 服务接口
 *
 * @author makejava
 * @since 2022-11-24 17:46:20
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hostList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult hotTest();

    ResponseResult updateViewCount(Long id);
}

