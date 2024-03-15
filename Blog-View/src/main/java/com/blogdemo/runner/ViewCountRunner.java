package com.blogdemo.runner;

import com.blogdemo.mapper.ArticleDao;
import com.blogdemo.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.blogdemo.domain.entity.Article;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务启动时自动把浏览量复制到redis中
 *
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception{
        //查询博客信息  id  viewCount
        List<Article> articles = articleDao.selectList(null);
        Map<String, Object> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()));
        //存储到redis中
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }

}