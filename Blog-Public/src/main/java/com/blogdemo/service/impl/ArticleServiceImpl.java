package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.constants.SystemConstants;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Category;
import com.blogdemo.domain.vo.ArticleDetailVO;
import com.blogdemo.domain.vo.ArticleListVO;
import com.blogdemo.domain.vo.HotArticleListVO;
import com.blogdemo.domain.vo.PageVO;
import com.blogdemo.mapper.ArticleDao;
import com.blogdemo.domain.entity.Article;
import com.blogdemo.service.ArticleService;
import com.blogdemo.service.CategoryService;
import com.blogdemo.utils.BeanCopyUtilsVO;
import com.blogdemo.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author VMlee
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 热门文章列表，根据浏览量降序查询
     * 暂时不改动从redis中获取
     */
    @Override
    public ResponseResult hostList() {
        //开始创建查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        /*
         * 查询条件状态为0的值：
         * 通过lambda表达式查询Article类中成员变量status中get方法getStatus去获取字段
         * 通过设定常量 ARTICLE_STATUS_NORMAL=0 表示需要数值
        */
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        //查询条件：浏览量字段降序
        queryWrapper.orderByDesc(Article::getViewCount);

        //使用mybatis自带的分页功能,展示所有结果中前十条数据
        Page<Article> page = new Page(1,10);
        // 页面内容，查询信息
        page(page,queryWrapper);

        List<Article> articleList = page.getRecords();

        List<HotArticleListVO> hotArticleListVO = BeanCopyUtilsVO.copyBeanList(articleList, HotArticleListVO.class);

//        /*
//         * 通过VO筛选 articleList 中的数据返回给前端
//         * 源数据 <articleList> -> vo筛选后的数据<hotArticleListVO>
//         * 返回筛选后的数据<hotArticleListVO>
//         */
//        List<HotArticleListVO> hotArticleListVO = new ArrayList<>();
//        for(Article article : articleList){
//            HotArticleListVO vo = new HotArticleListVO();
//
//            //bean拷贝,只拷贝相同字段的数据,将articleList从数据库中得到的数据根据VO类筛选到hotArticleList数组中
//            BeanUtils.copyProperties(article,vo);
//            hotArticleListVO.add(vo);
//        }

            return ResponseResult.okResult(hotArticleListVO);
    }
    /**
     * 文章表(articleList)文章列表
     *  如果有分页id，查询时需要
     *  文章状态是正式发布
     *  用分页查询，可以节省首页开销，没必要全部展示
     *  对 isTop字段进行降序
     *  改动--从redis中获取浏览量
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //开始创建查询
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        /*
         *查询条件:如果有categoryID,查询时需要传入
         * condition:条件 如果有 categoryId不为空且大于0 ,运行后面的查询,没有则不运行
         */
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);

        //查询条件:状态为正常,不为草稿
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //降序isTop,置顶最先显示
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //创建mybatis的分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);

        //开始查询categoryName
        page(page,lambdaQueryWrapper);
        List<Article> articles = page.getRecords();

        //stream()流处理把上面articles集合中 存在的每一个categoryName赋值为categoryService中查询到的id的名字 .collect()终结放回articles
        articles.stream().peek(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());


        // 文章列表返回vo需要的字段，此时articles集合地址已经和page.getRecords()一致,这两个其中一个参数都可以表示获取的集合地址并且categoryName已被赋值
        List<ArticleListVO> articleListVOS = BeanCopyUtilsVO.copyBeanList(page.getRecords(), ArticleListVO.class);

        //从redis中获取浏览量,直接改掉VO类里面的浏览量，返回给前台
        for (ArticleListVO article : articleListVOS) {
            Integer redisViewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(redisViewCount.longValue());
        }

        //再使用PageVO 封装 articleListVOS 内容 + total字段返回内容
        PageVO pageVO = new PageVO(articleListVOS,page.getTotal());


        return ResponseResult.okResult(pageVO);
    }

    /**
     * 文章详情页面
     * 改动--从redis中获取浏览量
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查文章
        Article article = getById(id);

        // 从redis中获取浏览量
        Integer getViewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(getViewCount.longValue());

        // 转换VO
        ArticleDetailVO articleDetailVO = BeanCopyUtilsVO.copyBean(article, ArticleDetailVO.class);
        //根据id查询分类名
        Long categoryId = articleDetailVO.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVO.setCategoryName(category.getName());
        }
        //封装响应结果返回

        return ResponseResult.okResult(articleDetailVO);
    }

    /**
     * 仅用于学习测试的热门文章列表
     * @return
     */
    @Override
    public ResponseResult hotTest() {
        //开始创建查询
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        //查询条件：Article类中的getStatus字段，ARTICLE_STATUS_NORMAL 常量类中预先设置值为0,表示为已发表文章。值为1表示未发表,此处不需要
        queryWrapper.eq("status",SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc("view_count");

        //使用mybatis自带的分页功能,展示所有结果中前十条数据
        Page<Article> pageResult = new Page(1,10);

        //查询信息
        List<Article> articleList = page(pageResult,queryWrapper).getRecords();

        List<HotArticleListVO> hotArticleListVO =new ArrayList<>();
        for(Article article:articleList) {
            HotArticleListVO vo = new HotArticleListVO();
            BeanUtils.copyProperties(article,vo);
            hotArticleListVO.add(vo);
        }
        return ResponseResult.okResult(hotArticleListVO);
    }

    /**
     * 在redis中更新浏览量
     * @param id
     * @return ok
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", String.valueOf(id),1);
        return ResponseResult.okResult();
    }

}

