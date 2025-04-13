package com.tech.doc.spring.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.tech.doc.spring.annotation.PerfLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ArpatNurmamat <airepatinuermaimaiti@kuaishou.com>
 * Created on 2025-04-13
 */

 @Aspect
 @Component
 @Slf4j
public class PerfLogAspect {


    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PerfLog perfLog = method.getAnnotation(PerfLog.class);

        String logId = perfLog.value().isEmpty() 
        ? method.getDeclaringClass().getSimpleName() + "." + method.getName()
        : perfLog.value();

        if (perfLog.logParams()) {
            System.out.printf("[{%s}] 方法开始执行，参数: {%s}", logId, Arrays.toString(joinPoint.getArgs()));
        } else {
            log.info("[{}] 方法开始执行", logId);
        }

        // 记录执行时间
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            
            // 记录执行结果
            long executionTime = System.currentTimeMillis() - startTime;
            if (perfLog.logReturn()) {
                log.info("[{}] 方法执行完成，耗时: {}ms，返回值: {}", logId, executionTime, result);
            } else {
                log.info("[{}] 方法执行完成，耗时: {}ms", logId, executionTime);
            }
            
            return result;
        } catch (Throwable e) {
            // 记录异常信息
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("[{}] 方法执行异常，耗时: {}ms, 异常: {}", logId, executionTime, e.getMessage());
            throw e;
        }
    }

}