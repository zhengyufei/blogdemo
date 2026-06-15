package edu.lzy.cyx.blogdemo.dao;

import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import edu.lzy.cyx.blogdemo.service.IArticleService;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StatisticMapper {

    @Select("select * from t_statistic where hits != '0' order by hits desc,comments_num desc")
    public List<Statistic> getStatistic();

    @Select("select * from t_statistic where article_id=#{articleId}")
    public Statistic selectStatisticWithArticleId(Integer articleId);

    @Update("update t_statistic set hits=#{hits} where article_id=#{articleId}")
    public void updateArticleHitsWithId(Statistic statistic);

    @Update("update t_statistic set comments_num=#{commentsNum} where article_id=#{articleId}")
    public void updateArticleCommentWithId(Statistic statistic);

    @Insert("insert into t_statistic(article_id,hits,comments_num) values (#{id},0,0)")
    public void addStatistic(Article article);

    @Delete("delete from t_statistic where article_id=#{id}")
    int deleteStatisticWithId(int id);

    @Select("SELECT SUM(hits) FROM t_statistic")
    public long getTotalVisit();

    @Select("SELECT SUM(comments_num) FROM t_statistic")
    public long getTotalComment();



}
