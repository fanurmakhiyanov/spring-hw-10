package ru.gb.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ArgumentLoggerAspect {

    private LocalDateTime start;

    @Pointcut("@annotation(Timer)")
    public void callTimer(){}

    @Before("callTimer()")
    void beforeStartTimer(JoinPoint joinPoint){
        start = LocalDateTime.now();
    }
    @After("callTimer()")
    void afterEndTimer(JoinPoint joinPoint){
        Duration result = Duration.between(start,LocalDateTime.now());

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().toShortString();

        log.info("{} - {} #{}",className,methodName,result.toString());
    }
}