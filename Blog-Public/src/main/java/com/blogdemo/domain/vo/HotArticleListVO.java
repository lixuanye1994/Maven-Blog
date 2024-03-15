package com.blogdemo.domain.vo;

/**
 * 热门文章列表的VO类，dao层数据库获取数据后，用VO类筛选数据
 */
public class HotArticleListVO {
    private Long id;
    private String title;
    private Long viewCount;

    /**
     * 保留无参构造方法
     */
    public HotArticleListVO(){

    }

    /**
     * VO类有参构造方法
     */
    public HotArticleListVO(Long id,String title,Long viewCount){
        this.id = id;
        this.title = title;
        this. viewCount = viewCount;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
