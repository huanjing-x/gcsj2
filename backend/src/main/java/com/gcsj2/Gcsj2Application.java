package com.gcsj2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码片段管理系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.gcsj2.mapper")
public class Gcsj2Application {

    public static void main(String[] args) {
        SpringApplication.run(Gcsj2Application.class, args);
    }
}
