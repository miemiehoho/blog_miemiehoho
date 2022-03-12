package com.miemiehoho.blog.handler;

import com.miemiehoho.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author miemiehoho
 * @date 2021/11/18 9:17
 */
@ControllerAdvice//对加了controller注解的方法进行拦截处理 AOP的实现
public class AllExceptionHandler {
    // 进行异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody// 返回json数据，不加这个注解返回的是页面
    public Result doException(Exception e) {
        e.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
