package com.wjc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ViewController implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/manage/myPost").setViewName("toshare/user/managePost");
//    }

    @Bean
    public WebMvcConfigurer mvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                //registry.addViewController("/user/registration").setViewName("toshare/user/regist");
                //registry.addViewController("/login").setViewName("toshare/user/login");
                registry.addViewController("/").setViewName("toshare/user/login");//静态资源访问
            }
        };
    }
}
