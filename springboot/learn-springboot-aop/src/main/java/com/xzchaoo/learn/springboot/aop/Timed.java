package com.xzchaoo.learn.springboot.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zcxu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timed {
    String scenario();

    String action();

    /**
     * 如果不提供 则用方法名 目前不支持重载版本
     *
     * @return
     */
    String op() default "";
}
