package com.blog.admin.service.impl;

import com.blog.admin.dao.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author miemiehoho
 * @date 2022/1/13 11:38
 */
@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    // 登录的时候会把username传递到这里
    // 通过username查询admin表，如果admin存在，将密码告诉spring security
    // 如果不存在返回null(也就是认证失败）
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null) {
            return null;// 登陆失败
        }
        UserDetails userdetails = new User(username, admin.getPassword(), new ArrayList<>());
        return userdetails;
    }
}
