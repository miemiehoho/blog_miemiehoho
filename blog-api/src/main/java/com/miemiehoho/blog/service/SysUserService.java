package com.miemiehoho.blog.service;

import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.UserVo;

/**
 * @author miemiehoho
 * @date 2021/11/17 19:49
 */
public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
