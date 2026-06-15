package edu.lzy.cyx.blogdemo.utils;

import edu.lzy.cyx.blogdemo.model.domain.Article;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Commons {


    /**
     * 网站链接
     */
    public String site_url() {
        return site_url("/page/1");
    }

    /**
     * 返回网站链接下的全址
     */
    public String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    /**
     * 网站配置项
     */
    public String site_option(String key) {
        return site_option(key, "");
    }

    /**
     * 网站配置项
     */
    public String site_option(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return defalutValue;
    }

    /**
     * 截取字符串
     */
    public String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 返回日期
     */
    public String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 返回文章链接地址
     */
    public String permalink(Integer aid) {
        return site_url("/article/" + aid.toString());
    }

    /**
     * 截取文章摘要
     */
    public String intro(Article article, int len) {
        String value = article.getContent();
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return ArticleUtils.htmlToText(ArticleUtils.mdToHtml(html));
        } else {
            String text = ArticleUtils.htmlToText(ArticleUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len)+"......";
            }
            return text;
        }
    }

    /**
     * 对文章内容进行格式转换，将Markdown为Html
     */
    public String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return ArticleUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 显示文章缩略图
     */
    public String show_thumb(Article article) {
        if (StringUtils.isNotBlank(article.getThumbnail())){
            return article.getThumbnail();
        }
        int cid = article.getId();
        int size = cid % 24;
        size = size == 0 ? 1 : size;
        return "/user/img/rand/" + size + ".png";
    }

    /**
     * 转换emoji表情
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }
}