package com.miemiehoho.blog.dao.pojo;

import lombok.Data;

/**
 * @author miemiehoho
 * @date 2021/11/17 10:47
 */
@Data
public class SysUser {
//    @TableId(type = IdType.ASSIGN_ID)// 默认id类型
    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}
