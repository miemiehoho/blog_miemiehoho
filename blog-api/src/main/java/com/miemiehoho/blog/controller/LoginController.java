package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.service.SysUserService;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.LoginParam;
import com.miemiehoho.blog.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author miemiehoho
 * @date 2021/11/18 10:53
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;//不同的service对应不同业务，登录并不是user的业务

    @Autowired
    private LoginService loginService;// 为登录这个业务写一个业务service

    @ApiOperation("登录")
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }
}
