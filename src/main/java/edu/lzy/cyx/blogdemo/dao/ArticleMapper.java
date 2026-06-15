package edu.lzy.cyx.blogdemo.dao;

import edu.lzy.cyx.blogdemo.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("select * from t_article order by id desc")
    public List<Article> selectArticleWithPage();

    //根据ID查找article
    @Select("select * from t_article where id=#{id}")
    public Article selectArticleWithId(Integer id);

    @Select("select count(1) from t_article")
    public Integer countArticle();

    @Insert("insert into t_article(title,content,created,categories,tags,allow_comment,thumbnail) values (#{title},#{content},#{created},#{categories},#{tags},#{allowComment},#{thumbnail})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public Integer publishArticle(Article article);

    int updateArticleWithId(Article article);

    @Delete("delete from t_article where id=#{id}")
    int deleteArticleWithId(int id);
}
