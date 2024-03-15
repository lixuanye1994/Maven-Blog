package com.blogdemo.controller;

import com.blogdemo.aop.BlogSystemLog;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Article;
import com.blogdemo.service.ArticleService;
import com.blogdemo.utils.RedisCache;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章模块接口
 * /article 主入口
 * /list 文章列表查询 测试接口 已删除代码
 * /hotList 博客页面侧边栏热门文章查询
 * /articleList 首页文章预览查询
 * /{id} 根据文章具体id查看单个文章详细内容
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    /**
     * 热门文章列表接口代码
     * 在GetMapping中添加produces = "application/json;charset=utf-8"
     * 可使得返回值为json格式显示更美观
     */
    @BlogSystemLog(BusinessName = "热门文章列表")
    @GetMapping(value = "/hotList",produces = "application/json;charset=utf-8")
    public ResponseResult hotList(){
        return articleService.hostList();
    }
    /**
     * 文章列表分页显示接口代码
     * @param pageNum
     * @param pageSize
     * @param categoryId
     */
    @BlogSystemLog(BusinessName = "首页文章列表")
    @GetMapping("/articleList")
    //定义Integer字段和Long字段的优势,如果前端没有传入相应参数,程序不会报错,并且返回值为null,就可以根据返回值去做判断等操作
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 文章详情功能接口代码
     * @param id
     */
    @BlogSystemLog(BusinessName = "文章详情功能")
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 定时更新浏览量到服务器中
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }


    @GetMapping("/test")
    public ResponseResult hotTest(){
        return articleService.hotTest();
    }
}
