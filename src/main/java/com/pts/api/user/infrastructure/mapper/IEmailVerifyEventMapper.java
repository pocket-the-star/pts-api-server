package com.pts.api.user.infrastructure.mapper;

public interface IEmailVerifyEventMapper<T> {

    String mapToString(T object);

    T mapToObject(String value, Class<T> clazz);
}
