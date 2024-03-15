package com.blogdemo.settingtime;

import com.blogdemo.domain.entity.Article;
import com.blogdemo.service.ArticleService;
import com.blogdemo.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;


    @Scheduled(cron = "0/55 * * * * ?")
    public void UpdateViewCountJob(){
        // 获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");

        // 双列集合不能使用stream流，使用entrySet()改成单列集合
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),entry.getValue().longValue()))
                .collect(Collectors.toList());

        // 10分钟一次更新到数据库
        articleService.updateBatchById(articles);
        System.out.println("浏览量更新完成");
    }
}
