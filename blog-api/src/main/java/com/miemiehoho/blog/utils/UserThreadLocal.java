package com.miemiehoho.blog.utils;

import com.miemiehoho.blog.dao.pojo.SysUser;

/**
 * @author miemiehoho
 * @date 2021/11/18 19:44
 */
// 饿汉式
public class UserThreadLocal {

    private UserThreadLocal() {
    }// 设置为私有的，不能new

    // 线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
