package edu.lzy.cyx.blogdemo.web.client;


import com.vdurmont.emoji.EmojiParser;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.responsedata.ArticleResponseData;
import edu.lzy.cyx.blogdemo.service.ICommentService;
import edu.lzy.cyx.blogdemo.utils.MyUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private ICommentService commentService;
@PostMapping(value = "/publish")
@ResponseBody
    public ArticleResponseData publishComment(HttpServletRequest request,
                                              @RequestParam Integer aid,
                                              @RequestParam(value = "author", required = false) String author,
                                              @RequestParam String text){
        text = StringUtils.trimToEmpty(text);
        if (StringUtils.isBlank(text)) {
            return ArticleResponseData.fail("评论内容不能为空");
        }
        author = StringUtils.defaultIfBlank(StringUtils.trimToEmpty(author), "匿名用户");
        author = StringUtils.substring(MyUtils.cleanXSS(author), 0, 20);
        text = MyUtils.cleanXSS(text);
        text = EmojiParser.parseToAliases(text);
        Comment comment = new Comment();
        comment.setArticleId(aid);
        comment.setIp(request.getRemoteAddr());
        comment.setCreated(new Date());
        comment.setAuthor(author);
        comment.setContent(text);
        comment.setStatus("approved");
        try {
            commentService.publishComment(comment);
            logger.info("评论成功，对应文章ID："+aid);
            return ArticleResponseData.ok();
        }catch (Exception e){
            logger.error("发表评论失败，对应文章ID："+aid+";错误描述："+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
