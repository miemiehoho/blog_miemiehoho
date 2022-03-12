package com.blog.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author miemiehoho
 * @date 2022/1/12 14:46
 */
@Configuration// 标示为配置类
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 密码配置
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        //加密策略 MD5 不安全 彩虹表  MD5 加盐
        String encode = new BCryptPasswordEncoder().encode("admin");
        System.out.println(encode);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()// 开启登录认证
                .antMatchers("/css/**").permitAll()//放行
                .antMatchers("/img/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/admin/**").access("@authService.auth(request,authentication)")//自定义service 来去实现实时的权限认证
                .antMatchers("/articles/**").access("@authService.auth(request,authentication)")
                .antMatchers("/pages/**").authenticated()
                .and().formLogin()
                .loginPage("/login.html")// 自定义的登陆页面
                .loginProcessingUrl("/login")// 登陆处理接口
                .usernameParameter("username")// 定义登陆时的用户名的key，默认是username
                .passwordParameter("password")// 定义登陆时密码的key，默认是password
                .defaultSuccessUrl("/pages/main.html")
                .failureUrl("/login.html")//登陆失败的跳转页面
                .permitAll()//通过 不拦截，更加前面配的路径决定，这是指和登录表单相关的接口 都通过
                .and().logout()// 退出登陆配置
                .logoutUrl("/logout")// 退出登陆接口
                .logoutSuccessUrl("/login.html")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()////csrf关闭 如果自定义登录 需要关闭； csrf 用于检测跨站伪造攻击
                .headers().frameOptions().sameOrigin();// 支持iframe页面嵌套
    }
}
