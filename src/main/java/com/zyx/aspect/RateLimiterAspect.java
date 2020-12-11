package com.zyx.aspect;


import com.zyx.annotation.RateLimit;
import com.zyx.limiter.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;


@Aspect
@Component
public class RateLimiterAspect {


    @Autowired
    private RateLimiter rateLimiter;


    /**
     * 业务层切点
     */
    @Pointcut("@annotation(com.zyx.annotation.RateLimit)")
    public void ServiceAspect() { }

    @Around("ServiceAspect()")
    public Object before(ProceedingJoinPoint joinPoint) {
        int arg = 0;
        String name = joinPoint.getSignature().getName();
        Class<?> cl = joinPoint.getTarget().getClass();
        for(Method method:cl.getMethods()){
            if (name.equals(method.getName())) {
                arg = Integer.valueOf(method.getAnnotation(RateLimit.class).value());
            }
        }
        Object obj = null;
        try {
            //tryAcquire()是非阻塞, rateLimiter.acquire()是阻塞的
            if (rateLimiter.acquire(arg) > 0) {
                System.out.println("执行方法");
                obj = joinPoint.proceed();
            } else {
                //拒绝了请求（服务降级）
                obj = "The system is busy, please visit after a while";
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
