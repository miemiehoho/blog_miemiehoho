package com.miemiehoho.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.miemiehoho.blog.vo.ErrorCode;
import com.miemiehoho.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author miemiehoho
 * @date 2022/1/11 23:24
 */
@Aspect// 定义一个切面，切面定义了通知和切点的关系
@Component
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 切点（这个注解加在哪里，哪个方法就是切点）
    @Pointcut("@annotation(com.miemiehoho.blog.common.cache.Cache)")
    public void pt() {
    }

    // 环绕通知，关联了切点
    @Around("pt()")
    public Object arount(ProceedingJoinPoint joinPoint) {
        try {
            Signature signature = joinPoint.getSignature();
            // 类名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            // 方法名
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[joinPoint.getArgs().length];// 声明参数类型数组
            Object[] args = joinPoint.getArgs();// 拿到参数数组

            // 参数
            String params = "";// 根据不同的参数设定不同的key
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();// 参数类型
                } else {
                    parameterTypes[i] = null;
                }
            }


            if (StringUtils.isNotEmpty(params)) {
                // 加密，防止出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }

            // 拿到对应的方法
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            Cache annotation = method.getAnnotation(Cache.class);// 通过对应的方法拿到对应的cache注解
            long expire = annotation.expire();// 缓存过期时间
            String name = annotation.name();// 缓存名称
            // 先从 redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)) {// 如果有缓存直接返回
             //      log.info("走了缓存---,{},{}", className, methodName);
                log.info("走了缓存---,{},{}", redisKey);
                return JSON.parseObject(redisValue, Result.class);
            }
            Object proceed = joinPoint.proceed();// 没有缓存，直接调用方法
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));// 把方法的返回结果存入缓存
            log.info("存入缓存---,{},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }

}
