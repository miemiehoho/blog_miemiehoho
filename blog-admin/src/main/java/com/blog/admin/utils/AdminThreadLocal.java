package com.blog.admin.utils;

import com.blog.admin.dao.pojo.Admin;

/**
 * @author miemiehoho
 * @date 2022/3/8 11:45
 */
// 饿汉式
public class AdminThreadLocal {
    private AdminThreadLocal() {

    }

    // 线程变量隔离
    private static final ThreadLocal<Admin> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(Admin admin) {
        THREAD_LOCAL.set(admin);
    }

    public static Admin get() {
        Admin admin = THREAD_LOCAL.get();
        return admin;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
