package com.miemiehoho.blog.service;

import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.params.LoginParam;

/**
 * @author miemiehoho
 * @date 2021/11/18 11:04
 */
public interface LoginService {

    public Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
