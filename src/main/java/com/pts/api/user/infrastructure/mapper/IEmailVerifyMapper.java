package com.pts.api.user.infrastructure.mapper;

public interface IEmailVerifyMapper<T> {

    String mapToString(T object);

    T mapToObject(String value, Class<T> clazz);
}
