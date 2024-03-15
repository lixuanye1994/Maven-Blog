package com.blogdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blogdemo.domain.entity.Article;
import org.springframework.stereotype.Repository;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-24 17:46:19
 */
@Repository("articleDao")
public interface ArticleDao extends BaseMapper<Article> {

}

