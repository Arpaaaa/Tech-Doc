package com.tech.doc.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 性能日志注解，用于记录方法执行时间和相关信息
 * @author ArpatNurmamat <airepatinuermaimaiti@kuaishou.com>
 * Created on 2025-04-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerfLog {
    /**
     * 日志标识，用于区分不同方法的日志
     */
    String value() default "";
    
    /**
     * 是否记录方法参数
     */
    boolean logParams() default true;

    boolean logReturn() default true;
    
}