package edu.lzy.cyx.blogdemo.dao;

import edu.lzy.cyx.blogdemo.model.domain.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("select * from t_comment where article_id=#{aid} order by id desc")
    List<Comment> selectCommentWithPage(Integer aid);

    // 👇 这是修复后的正确 SQL
    @Insert("insert into t_comment(article_id,created,author,ip,content,status) " +
            "values(#{articleId},#{created},#{author},#{ip},#{content},#{status})")
    void publishComment(Comment comment);

    @Select("select * from t_comment order by id desc")
    public List<Comment> selectNewComment();

    @Select("select count(1) from t_comment")
    public Integer countComment();


    @Delete("delete from t_comment where article_id=#{id}")
    int deleteCommentWithId(int id);
}