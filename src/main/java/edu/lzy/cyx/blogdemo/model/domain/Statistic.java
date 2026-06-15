package edu.lzy.cyx.blogdemo.model.domain;

import lombok.Data;

@Data
public class Statistic {
    private Integer id;
    private Integer articleId;
    private Integer hits;
    private Integer commentsNum;
}
