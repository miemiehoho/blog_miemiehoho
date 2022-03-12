package com.miemiehoho.blog.config;

import com.miemiehoho.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author miemiehoho
 * @date 2021/11/17 9:49
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域配置：前端8080-后端8888
        registry.addMapping("/**").allowedOrigins("http://localhost:8081");//定义所有接口，允许域名：http://localhost:8080访问
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截接口,先拦截test，以后有需要时再改成需要拦截的
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/swagger-resources");
    }
}
