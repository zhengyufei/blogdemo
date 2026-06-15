package edu.lzy.cyx.blogdemo.service.impl;

import com.github.pagehelper.PageHelper;
import edu.lzy.cyx.blogdemo.dao.ArticleMapper;
import edu.lzy.cyx.blogdemo.dao.CommentMapper;
import edu.lzy.cyx.blogdemo.dao.StatisticMapper;
import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import edu.lzy.cyx.blogdemo.responsedata.StatisticsBo;
import edu.lzy.cyx.blogdemo.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements ISiteService {
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public void updateStatistic(Article article){
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }

    @Override
    public List<Comment> recentComments(int count){
        PageHelper.startPage(1, count>10 || count<1 ? 10 : count);
        List<Comment> byPage = commentMapper.selectNewComment();
        return byPage;
    }

    @Override
    public List<Article> recentAriticles(int count) {
        PageHelper.startPage(1,count>10 || count<1 ? 10 :count);
        List<Article> list = articleMapper.selectArticleWithPage();
        for (int i = 0; i<list.size(); i++){
            Article article = list.get(1);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }

    @Override
    public StatisticsBo getStatistics(){
        StatisticsBo statisticsBo = new StatisticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        statisticsBo.setArticles(articles);
        statisticsBo.setComments(comments);
        return statisticsBo;
    }
}
