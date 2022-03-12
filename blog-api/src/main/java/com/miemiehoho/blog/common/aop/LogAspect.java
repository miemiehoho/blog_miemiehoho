package com.miemiehoho.blog.common.aop;

import com.alibaba.fastjson.JSON;
import com.miemiehoho.blog.dao.mapper.SysLogMapper;
import com.miemiehoho.blog.dao.pojo.SysLog;
import com.miemiehoho.blog.utils.UserThreadLocal;
import com.miemiehoho.blog.utils.HttpContextUtils;
import com.miemiehoho.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author miemiehoho
 * @date 2022/1/10 11:25
 */
@Component
@Aspect// 切面，定义了通知和切点的关系
@Slf4j
public class LogAspect {

    // 定义一个切点,注解加在哪，哪里就是切点
    @Pointcut("@annotation(com.miemiehoho.blog.common.aop.LogAnnotation)")
    public void pt() {
    }


    // 定义通知类,环绕通知(可以对方法前后都进行一个增强）
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法(调用原有方法）
        Object result = joinPoint.proceed();
        // 执行时长（毫秒）
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        recordLog(joinPoint, time);
        return result;
    }

    @Autowired
    private SysLogMapper sysLogMapper;

    // 记录日志
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        // 请求发方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);

        // 获取request 设置 ip 地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IpUtils.getIpAddr(request);

        SysLog sysLog = new SysLog();
        sysLog.setCreateDate(System.currentTimeMillis());
        sysLog.setIp(ip);
        sysLog.setMethod(methodName);
        sysLog.setModule(logAnnotation.module());
        sysLog.setOperation(logAnnotation.operator());
        sysLog.setParams(params);
        sysLog.setTime(time);
        if (UserThreadLocal.get() != null) {
            sysLog.setNickname(UserThreadLocal.get().getNickname());
            sysLog.setUserId(UserThreadLocal.get().getId());
        }
        sysLogMapper.insert(sysLog);
    }
}
