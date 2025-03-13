package com.pts.api.user.infrastructure.mapper;

import com.pts.api.user.domain.model.EmailVerify;

public interface IEmailVerifyMapper {

    String mapToString(EmailVerify object);

    EmailVerify mapToObject(String value);
}
