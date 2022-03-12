package com.miemiehoho.blog.dao.pojo;

import lombok.Data;

/**
 * @author miemiehoho
 * @date 2022/3/8 20:09
 */
@Data
public class SysLog {

    private Long id;
    private Long createDate;
    private String ip;
    private String method;
    private String module;
    private String nickname;
    private String operation;
    private String params;
    private Long time;
    private Long userId;
}
