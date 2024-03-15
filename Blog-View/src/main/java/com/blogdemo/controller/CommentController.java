package com.blogdemo.controller;

import com.blogdemo.constants.SystemConstants;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Comment;
import com.blogdemo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @GetMapping("/linkCommentList")
    @ApiOperation(value="友链评论列表",notes="友链评论相关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量"),
    })
    public ResponseResult linkCommentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.lINK_COMMENT,null,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);

    }
}
