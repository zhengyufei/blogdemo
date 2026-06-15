package edu.lzy.cyx.blogdemo.web.client;

import com.github.pagehelper.PageInfo;
import edu.lzy.cyx.blogdemo.dao.ArticleMapper;
import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.service.IArticleService;
import edu.lzy.cyx.blogdemo.service.ICommentService;
import edu.lzy.cyx.blogdemo.service.ISiteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ISiteService siteService;
    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        return this.index(request,1,5);
    }

    @GetMapping(value= "/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<Article> articles = articleService.selectArticleWithPage(page, count);
        List<Article> articleList = articleService.getHeatArticles();
        request.setAttribute("articles", articles);
        request.setAttribute("articleList", articleList);
        logger.info("分页获取文章信息：页码"+page+"，条数"+count);
        return "client/index";
    }

    @GetMapping("/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id,HttpServletRequest request){
        Article article = articleService.selectArticleWithId(id);
        if (article != null){
            getArticleComments(request,article);
            siteService.updateStatistic(article);
            request.setAttribute("article",article);
            return "client/articleDetails";
        }else {
            logger.warn("查询文章详情结果为空，文章id:"+id);
            return "comm/error_404";
        }
    }

    // ✅ 修复完成！这里是关键！
    private void getArticleComments(HttpServletRequest request,Article article){
        if (article.getAllowComment()){
            String commentPage = request.getParameter("cp");
            commentPage = StringUtils.isBlank(commentPage)?"1":commentPage;

            PageInfo<Comment> comments = commentService.getComments(article.getId(),Integer.parseInt(commentPage),3);

            // ✅ 把评论放进请求域，页面才能显示！
            request.setAttribute("comments", comments);
            request.setAttribute("cp",commentPage);
        }
    }
}