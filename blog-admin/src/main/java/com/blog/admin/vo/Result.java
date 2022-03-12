package com.blog.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求的返回值
 *
 * @author miemiehoho
 * @date 2021/11/17 10:57
 */
@Data
@AllArgsConstructor//自动生成所有构造方法
public class Result {

    private boolean success;
    private int code;
    private String msg;
    private Object data;

    // 成功的方法
    public static Result success(Object data) {
        return new Result(true, 200, "success", data);
    }

    // 失败
    public static Result fail(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}
