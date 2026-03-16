package com.luna.warmteaandhonestreviews.advisor;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ControllerLoggingAdvisor {

    @Around("execution(* com.luna.warmteaandhonestreviews.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        IO.println(
            "Executing method: " + className + "." + methodName + " with args: " + Arrays.toString(
                joinPoint.getArgs()));

        Object result = joinPoint.proceed(); // Execute the target method

        long duration = System.currentTimeMillis() - startTime;
        IO.println(
            "Method " + methodName + " executed in " + duration + "ms. Result: " + result);

        return result;
    }
}
