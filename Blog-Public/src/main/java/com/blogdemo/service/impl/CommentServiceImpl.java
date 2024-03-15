package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.constants.SystemConstants;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.domain.vo.CommentVO;
import com.blogdemo.domain.vo.PageVO;
import com.blogdemo.exception.SystemException;
import com.blogdemo.mapper.CommentDao;
import com.blogdemo.domain.entity.Comment;
import com.blogdemo.service.CommentService;
import com.blogdemo.service.UserService;
import com.blogdemo.utils.BeanCopyUtilsVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 评论表(Comment)表服务实现类
 *
 * @author vmlee
 * @since 2023-01-09 14:28:05
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断,SystemConstants.ARTICLE_COMMENT.equals(commentType)查询评论属于哪边，文章评论还是友链评论
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.IS_ROOT_COMMENT);
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询所有根评论
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);
        List<CommentVO> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVO commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVO> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        PageVO pageVO = new PageVO(commentVoList,page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 编写评论，需要在登陆状态
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //安全性判断，评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NULL);
        }

        //mybatisplus自带数据库编写语句，用于保存编写内容到服务器
        save(comment);

        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return commentVos
     */
    private List<CommentVO> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVO> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVO> toCommentVoList(List<Comment> list){
        List<CommentVO> commentVos = BeanCopyUtilsVO.copyBeanList(list, CommentVO.class);
        //遍历vo集合

        //TODO 改写成stream流形式
        for (CommentVO commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=SystemConstants.IS_ROOT_COMMENT){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}

