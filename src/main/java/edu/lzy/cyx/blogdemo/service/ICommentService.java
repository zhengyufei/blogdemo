package edu.lzy.cyx.blogdemo.service;


import com.github.pagehelper.PageInfo;
import edu.lzy.cyx.blogdemo.model.domain.Comment;

public interface ICommentService {
    PageInfo<Comment> getComments(Integer aid, int page, int count);

    // 加上这一行
    void publishComment(Comment comment);
}
