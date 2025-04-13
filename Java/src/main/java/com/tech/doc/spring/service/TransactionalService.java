package com.tech.doc.spring.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tech.doc.spring.annotation.PerfLog;

/**
 * @author ArpatNurmamat <airepatinuermaimaiti@kuaishou.com>
 * Created on 2025-04-13
 */

 @Service
public class TransactionalService {

    /**
     * ”@Transactional“注解参数说明:
     * - propagation = Propagation.REQUIRES_NEW: 创建新事务,如果当前存在事务则挂起当前事务
     * - isolation = Isolation.READ_COMMITTED: 设置事务隔离级别为读已提交,只能读取已经提交的数据
     * - timeout = 10: 设置事务超时时间为10秒
     * - readOnly = true: 设置事务为只读事务,不允许修改数据
     * - rollbackFor = Exception.class: 发生Exception异常时回滚事务
     * - noRollbackFor = IOException.class: 发生IOException异常时不回滚事务
     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED
//    , timeout = 10, readOnly = true, rollbackFor = Exception.class, noRollbackFor = IOException.class)
    @PerfLog(value = "save", logParams = true, logReturn = true)
    public void save(String name) {
        System.out.println("TransactionalService.save");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @PerfLog(logReturn = true)
    public void process() {
        System.out.println("TransactionalService.process");
    }


}