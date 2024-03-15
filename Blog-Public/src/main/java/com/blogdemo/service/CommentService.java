package com.blogdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author vmlee
 * @since 2023-01-09 14:28:05
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

