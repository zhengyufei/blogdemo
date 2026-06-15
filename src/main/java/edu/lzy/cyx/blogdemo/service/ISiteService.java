package edu.lzy.cyx.blogdemo.service;

import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import edu.lzy.cyx.blogdemo.responsedata.StatisticsBo;

import java.util.List;

public interface ISiteService {

    public void updateStatistic(Article article);

    public List<Comment> recentComments(int count);

    public List<Article> recentAriticles(int count);

    public StatisticsBo getStatistics();

}
