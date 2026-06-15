package edu.lzy.cyx.blogdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.lzy.cyx.blogdemo.dao.CommentMapper;
import edu.lzy.cyx.blogdemo.dao.StatisticMapper;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import edu.lzy.cyx.blogdemo.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentpageInfo = new PageInfo<>(commentList);
        return commentpageInfo;
    }
    @Override
    public void publishComment(Comment comment){
        commentMapper.publishComment(comment);
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }
}