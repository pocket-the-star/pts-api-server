package com.pts.api.global.infrastructure.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomCacheAdvice {

    private final CustomCacheManager customCacheManager;

    @Around("@annotation(com.pts.api.global.infrastructure.cache.CustomCacheable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        CustomCacheable cacheable = findAnnotation(joinPoint);
        return customCacheManager.processCache(
            cacheable.prefix(),
            extractKeyArgs(joinPoint, cacheable.keys()),
            cacheable.ttlSeconds(),
            findReturnType(joinPoint),
            joinPoint::proceed
        );
    }

    private String[] extractKeyArgs(ProceedingJoinPoint joinPoint, String[] keyNames) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        return Arrays.stream(keyNames)
            .map(keyName -> IntStream.range(0, parameterNames.length)
                .filter(i -> parameterNames[i].equals(keyName))
                .mapToObj(i -> String.valueOf(args[i]))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "메서드에 존재하지 않는 파라미터명: " + keyName))
            )
            .toArray(String[]::new);
    }

    private CustomCacheable findAnnotation(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod().getAnnotation(CustomCacheable.class);

    }

    private Type findReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        return method.getGenericReturnType();
    }
}
