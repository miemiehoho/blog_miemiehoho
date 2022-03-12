package com.blog.admin.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author miemiehoho
 * @date 2022/1/12 11:20
 */
@Data
public class Permission {

    @TableId(type = IdType.AUTO)// 设置为自增id
    private Long id;

    private String name;

    private String path;

    private String description;

}
