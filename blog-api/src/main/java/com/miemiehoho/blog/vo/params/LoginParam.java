package com.miemiehoho.blog.vo.params;

import lombok.Data;

/**
 * 登录参数
 *
 * @author miemiehoho
 * @date 2021/11/18 11:06
 */
@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
