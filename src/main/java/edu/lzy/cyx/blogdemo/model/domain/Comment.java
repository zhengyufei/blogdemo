package edu.lzy.cyx.blogdemo.model.domain;

import lombok.Data;

import java.util.Date;
@Data
public class Comment {
    private Integer id;
    private Integer articleId;
    private Date created;
    private String ip;
    private String content;
    private String status;
    private String author;
}
