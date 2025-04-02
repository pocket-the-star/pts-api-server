package com.pts.api.global.infrastructure.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomCacheable {

    String prefix() default "";

    String[] keys() default {};

    long ttlSeconds() default 10;
}
