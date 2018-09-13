//package com.zd112.framework.annotation;
//
//import android.os.SystemClock;
//
//import com.zd112.framework.utils.LogUtils;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.reflect.Method;
//
///**
// * @author jiangshide
// * @Created by Ender on 2018/9/11.
// * @Emal:18311271399@163.com
// */
//@Aspect
//public class NetApiAspect {
//
//    //    @Pointcut("execution(* com.lqr.androidaopdemo.MainActivity.test(..))")
//    @Pointcut("execution(@com.zd112.framework.annotation.NetApi * *(..))")
//    public void pointcut() {
//
//    }
//
//    @Before("pointcut()")
//    public void before(JoinPoint point) {
//        System.out.println("@Before");
//    }
//
//    @Around("pointcut()")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        NetApi annotation = method.getAnnotation(NetApi.class);
//        String value = annotation.method();
//        LogUtils.e("-----------value:", value);
//        long beginTime = SystemClock.currentThreadTimeMillis();
//        joinPoint.proceed();
//        long endTime = SystemClock.currentThreadTimeMillis();
//        long duration = endTime - beginTime;
//        System.out.println("time：" + duration + "ms");
//    }
//
//    @After("pointcut()")
//    public void after(JoinPoint point) {
//        System.out.println("@After");
//    }
//
//    @AfterReturning("pointcut()")
//    public void afterReturning(JoinPoint point, Object returnValue) {
//    }
//
//    @AfterThrowing(value = "pointcut()", throwing = "ex")
//    public void afterThrowing(Throwable ex) {
//        LogUtils.e("ex:", ex);
//    }
//}
