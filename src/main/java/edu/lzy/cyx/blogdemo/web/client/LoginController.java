package edu.lzy.cyx.blogdemo.web.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 功能：
 * 作者：牟鑫燕
 * 日期：2026年06月08日
 */
@Controller
public class LoginController {
    @GetMapping(value="/login")
    public String login(HttpServletRequest request, Map map) {
        String referer = request.getHeader("Referer");
        String url = request.getParameter("url");
        if (url != null && !url.equals("")) {
            map.put("url", url);
        }else if (referer != null && referer.contains("/login")) {
            map.put("url", "");
        }else {
            map.put("url", "referer");
        }
        return "comm/login";
    }

    @GetMapping(value="/errorPage/{page}/{code}")
    public String AccessExceptionHandler(@PathVariable("page") String page,@PathVariable("code") String code) {
        return page+"/"+code;
    }
}