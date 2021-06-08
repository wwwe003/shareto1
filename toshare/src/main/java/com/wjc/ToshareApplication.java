package com.wjc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.wjc.user","com.wjc.genre"})
@MapperScan("com.wjc.*.mapper")
public class ToshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToshareApplication.class, args);
    }

}
