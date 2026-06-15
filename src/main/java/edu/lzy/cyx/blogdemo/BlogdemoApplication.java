package edu.lzy.cyx.blogdemo; // 必须是这个包！

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// 核心就是加上下面这行 @MapperScan
@SpringBootApplication
@EnableScheduling
@MapperScan("edu.lzy.cyx.blogdemo.dao")
public class BlogdemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogdemoApplication.class, args);
    }
}
