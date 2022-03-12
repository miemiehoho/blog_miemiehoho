package com.miemiehoho.blog.common.aop;

import java.lang.annotation.*;

/**
 * @author miemiehoho
 * @date 2022/1/10 11:20
 */

// TYPE 代表可以放在类上，METHOD代表可以放在方法上
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";
}
