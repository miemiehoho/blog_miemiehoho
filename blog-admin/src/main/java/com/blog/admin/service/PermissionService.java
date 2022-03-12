package com.blog.admin.service;

import com.blog.admin.dao.pojo.Permission;
import com.blog.admin.vo.Result;
import com.blog.admin.vo.params.PageParam;

/**
 * @author miemiehoho
 * @date 2022/1/12 11:16
 */
public interface PermissionService {

    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);

}
