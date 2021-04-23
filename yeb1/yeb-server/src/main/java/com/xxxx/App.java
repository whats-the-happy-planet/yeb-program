package com.xxxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.xxxx.server.mapper")
@EnableScheduling
public class App 
{
    public static void main( String[] args ){
        SpringApplication.run(App.class);
    }
}
