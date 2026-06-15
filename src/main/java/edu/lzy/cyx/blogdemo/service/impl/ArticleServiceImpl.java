package edu.lzy.cyx.blogdemo.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import edu.lzy.cyx.blogdemo.dao.ArticleMapper;
import edu.lzy.cyx.blogdemo.dao.CommentMapper;
import edu.lzy.cyx.blogdemo.dao.StatisticMapper;
import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import edu.lzy.cyx.blogdemo.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Override
    public List<Article> getHeatArticles() {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articleList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Article article = articleMapper.selectArticleWithId(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articleList.add(article);
            if (i >= 9) {
                break;
            }
        }
        return articleList;
    }

    @Override
    public Article selectArticleWithId(Integer id) {
        Article article = null;

        article = articleMapper.selectArticleWithId(id);

        return article;
    }

    @Override
    public void publish(Article article) {
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);
        articleMapper.publishArticle(article);
        statisticMapper.addStatistic(article);
    }

    @Override
    public void updateArticleWithId(Article article) {
        article.setModified(new Date());
        articleMapper.updateArticleWithId(article);
    }
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public void deleteArticleWithId(int id) {
        articleMapper.deleteArticleWithId(id);
//        redisTemplate.delete("article_" + id);
        statisticMapper.deleteStatisticWithId(id);
        commentMapper.deleteCommentWithId(id);}

}
