package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.service.LoginService;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author miemiehoho
 * @date 2021/11/18 16:46
 */
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result register(@RequestBody LoginParam loginParam) {
        // sso 单点登录 后期如果把注册登录功能提出去，（单独的服务，可以独立提供接口服务）
        return loginService.register(loginParam);
    }

}
