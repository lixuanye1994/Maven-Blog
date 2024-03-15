package com.blogdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogdemo.constants.SystemConstants;
import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.Article;
import com.blogdemo.domain.entity.Category;
import com.blogdemo.domain.vo.CategoryVO;
import com.blogdemo.mapper.CategoryDao;
import com.blogdemo.service.ArticleService;
import com.blogdemo.service.CategoryService;
import com.blogdemo.utils.BeanCopyUtilsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 分类表(Category)表服务实现类
 *
 * @author leexy
 * @since 2022-11-30 15:21:02
 *     /**
 *      *  当前需求：
 *      *  1.在前端显示文章分类列表
 *      *  2.只显示有正式发布文章的列表，没有文章的列表无需显示
 *      *  3.正常状态分类的文章
 *      *
 *
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {


    //注入文章表,准备从文章表开始查询
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表,状态为已发布的文章,里面有文章分类字段
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询Article -> getStatus 中为正常状态的文章
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //用列表接收查到的数据
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);

        /**
         * 获取文章的分类id,去重,用Stream().toSet()方法存入Set集合
         * Set 、List 、Map:
         * Set集合只能保存无序唯一值,List集合可以保存有序重复值,Map是键值对集合<k,v>k唯一
         */

        Set<Long> categoryID = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //根据id字段查询分类表获得名称
        List<Category> categories = listByIds(categoryID);
        categories = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装VO,只显示需要的字段
        List<CategoryVO> categoryVOS = BeanCopyUtilsVO.copyBeanList(categories, CategoryVO.class);

        return ResponseResult.okResult(categoryVOS);
    }
}

