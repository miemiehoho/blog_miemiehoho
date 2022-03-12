package com.blog.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.admin.dao.pojo.Admin;
import com.blog.admin.dao.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author miemiehoho
 * @date 2022/1/13 11:41
 */
@Component
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("SELECT * FROM `ms_permission` where id in(select permission_id from ms_admin_permission where admin_id = #{admin_id})")
    List<Permission> findPermissionByAdminId(Long adminId);
}
