package com.xzchaoo.learn.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zcxu
 */
@Aspect
@Component
public class XxxAspect {
    @Pointcut("@annotation(com.xzchaoo.learn.springboot.aop.Timed)")
    private void p1() {
    }

    @Around("p1()")
    public Object beforeP1(ProceedingJoinPoint pjp) throws Throwable {
        //TODO 不支持重载
        final MethodSignature ms = (MethodSignature) pjp.getSignature();
        final Method method = ms.getMethod();
        final Timed timed = method.getAnnotation(Timed.class);
        final String action = timed.action();
        final String op = timed.op().isEmpty() ? method.getName() : timed.op();
        //TODO Exception handle
        long begin = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println(action + "." + op + ".time=" + (end - begin));
            System.out.println(action + "." + op + ".error=" + 0);
        }
    }
}
