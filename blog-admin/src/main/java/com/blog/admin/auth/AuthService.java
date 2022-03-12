package com.blog.admin.auth;

import com.blog.admin.dao.pojo.Admin;
import com.blog.admin.dao.pojo.Permission;
import com.blog.admin.service.impl.AdminService;
import com.blog.admin.utils.AdminThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miemiehoho
 * @date 2022/1/13 17:56
 */
@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication) {
        // 权限认证
        String requestURI = request.getRequestURI();// 请求路径
        Object principal = authentication.getPrincipal();// 用户信息
        if (principal == null || "anonymousUser".equals(principal)) {// 如果用户信息为空或者为匿名用户
            // 未登录
            return false;
        }
        UserDetails userdetails = (UserDetails) principal;
        String username = userdetails.getUsername();
        Admin admin = adminService.findAdminByUserName(username);
        AdminThreadLocal.set(admin);
        if (admin == null) {
            return false;
        }
        if (1 == admin.getId()) {
            // 超级管理员
            return true;
        }
        Long id = admin.getId();
        List<Permission> permissionList = this.adminService.findPermissionByAdminId(id);
        requestURI = StringUtils.split(requestURI, '?')[0];
        for (Permission permission : permissionList) {
            if (requestURI.equals(permission.getPath())) {
                return true;
            }
        }
        return false;
    }
}
