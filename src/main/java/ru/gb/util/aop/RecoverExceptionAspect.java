package ru.gb.util.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RecoverExceptionAspect {

    @Pointcut("@annotation(RecoverException)")
    public void catchException(){}

    @Around("catchException()")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            if (Arrays.stream(getNoRecoverClass(joinPoint, e)).anyMatch(x -> x.isAssignableFrom(e.getClass()))) {
                return 0;
            }else throw e;
        }
    }

    private Class<?>[] getNoRecoverClass(JoinPoint joinPoint, Throwable e) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] methodArg = Arrays.stream(joinPoint.getArgs()).map(Object::getClass).toArray(Class[]::new);
        return joinPoint.getThis().getClass().getSuperclass()
                .getMethod(methodName, methodArg)
                .getAnnotation(RecoverException.class)
                .noRecoverFor();
    }
}