package com.pts.api.user.application.service.internal;

import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.response.TokenResponseDto;

public interface IGenerateTokenService {

    TokenResponseDto generate(Long userId, UserRole role);
}
