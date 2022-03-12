package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.service.SysUserService;
import com.miemiehoho.blog.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author miemiehoho
 * @date 2021/11/18 13:30
 */
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    SysUserService sysUserService;

    @ApiOperation("获取用户信息")
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }




}
