package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.utils.UserThreadLocal;
import com.miemiehoho.blog.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author miemiehoho
 * @date 2021/11/18 19:52
 */
@RestController
@RequestMapping("test")
public class TestController {


    @PostMapping
    public Result test() {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
