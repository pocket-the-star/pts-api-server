package com.pts.api.global.infrastructure.cache;

@FunctionalInterface
public interface CustomCacheOriginDataSupplier<T> {

    T get() throws Throwable;
}
