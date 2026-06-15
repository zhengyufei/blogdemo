package edu.lzy.cyx.blogdemo.service;


import com.github.pagehelper.PageInfo;
import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Comment;

import java.util.List;

public interface IArticleService {

    //分页查询文章列表
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer rows);

    //统计热门文章（前10）
    public List<Article> getHeatArticles();

    //根据文章id查询文章详情
    public Article selectArticleWithId(Integer id);

    void publish(Article article);
    public void updateArticleWithId(Article article);

    void deleteArticleWithId(int id);
}
