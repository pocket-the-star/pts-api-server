package com.pts.api.user.infrastructure.mapper;

import com.pts.api.lib.external.jpa.user.model.UserEntity;
import com.pts.api.user.domain.model.User;

public interface IUserMapper {

    User mapToModel(UserEntity userEntity);

    UserEntity mapToEntity(User user);
}
