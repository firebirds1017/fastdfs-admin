package com.fengsheng.fastdfs.config.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 实现切面，所有的service方法都被注入切点
 */
@Order(-1)
@Aspect
@Component
public class DaynamicDataSouceAop {

    /**
     * 没有Master注解和查询数据的切点
     */
    @Pointcut("!@annotation(com.fengsheng.fastdfs.config.datasource.Master) " +
            "&& (execution(* com.fengsheng.fastdfs.service..*.select*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.find*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.page*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.get*(..)))")
    public void readPointcut() {

    }
    /**
     *  Master注解和更改数据的切点
     */
    @Pointcut("@annotation(com.fengsheng.fastdfs.config.datasource.Master) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.insert*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.add*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.update*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.edit*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.upload*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.delete*(..)) " +
            "|| execution(* com.fengsheng.fastdfs.service..*.remove*(..))")
    public void writePointcut() {

    }

    /**
     * 切换到slave
     */
    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    /**
     * 切换到Master
     */
    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }
}