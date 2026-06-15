package edu.lzy.cyx.blogdemo.web.admin;

import com.github.pagehelper.PageInfo;
import edu.lzy.cyx.blogdemo.model.domain.Article;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.responsedata.ArticleResponseData;
import edu.lzy.cyx.blogdemo.responsedata.StatisticsBo;
import edu.lzy.cyx.blogdemo.service.IArticleService;
import edu.lzy.cyx.blogdemo.service.ISiteService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ISiteService siteService;
    @Autowired
    private IArticleService iArticleService;

    @GetMapping(value ={"","/index"})
    public String index(HttpServletRequest request){
        List<Article> articles = siteService.recentAriticles(5);
        List<Comment> comments = siteService.recentComments(5);
        StatisticsBo statisticsBo = siteService.getStatistics();
        request.setAttribute("articles",articles);
        request.setAttribute("comments",comments);
        request.setAttribute("statistics",statisticsBo);
        return "back/index";
    }
    @GetMapping("/article/toEditPage")
    public String newArticle(){
        return "back/article_edit";
    }

    @PostMapping(value = "/article/publish")
    @ResponseBody
    public ArticleResponseData publishArticle(Article article){
        if (StringUtils.isBlank(article.getCategories())){
            article.setCategories("默认分类");
        }
        try{
            iArticleService.publish(article);
            logger.info("博客发表成功");
            return ArticleResponseData.ok();
    }catch (Exception e){
            logger.error("博客发表失败，错误信息："+e.getMessage());
            return  ArticleResponseData.fail();
        }
    }
    @GetMapping(value = "/article")
    public String articleIndex(@RequestParam(value = "page",defaultValue = "1") int page,
                               @RequestParam(value = "count",defaultValue = "10") int count,
                               HttpServletRequest request){
        PageInfo<Article> pageInfo = iArticleService.selectArticleWithPage(page,count);
        request.setAttribute("articles", pageInfo);
        return "/back/article_list";
    }

    @GetMapping(value = "/article/{id}")
    public String editArticle(@PathVariable("id") String id, HttpServletRequest request) {
        Article article = iArticleService.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents", article);
        request.setAttribute("categories", article.getCategories());
        return "back/article_edit";
    }

    @PostMapping(value = "/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article) {
        try {
            iArticleService.updateArticleWithId(article);
            logger.info("文章更新成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章更新失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }}
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            iArticleService.deleteArticleWithId(id);
            logger.info("文章删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }}

}
