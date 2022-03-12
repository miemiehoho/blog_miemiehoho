package com.miemiehoho.blog.common.cache;

import java.lang.annotation.*;

/**
 * @author miemiehoho
 * @date 2022/1/11 23:20
 */
// TYPE 代表可以放在类上，METHOD代表可以放在方法上
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    // 缓存过期时间,默认1 * 60 * 1000;
    long expire() default 1 * 60 * 1000;

    // 缓存标识：key
    String name() default "";
}
