package edu.lzy.cyx.blogdemo.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Article {

    private Integer id;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private String categories;
    private String tags;
    private Boolean allowComment;
    private String thumbnail;

    private Integer hits;
    private Integer commentsNum;


}
