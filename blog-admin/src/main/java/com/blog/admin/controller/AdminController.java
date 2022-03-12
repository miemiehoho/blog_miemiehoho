package com.blog.admin.controller;

import com.blog.admin.dao.pojo.Permission;
import com.blog.admin.service.PermissionService;
import com.blog.admin.vo.Result;
import com.blog.admin.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author miemiehoho
 * @date 2022/1/12 11:12
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    PermissionService permissionService;

    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam) {
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission) {
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return permissionService.delete(id);
    }

}
