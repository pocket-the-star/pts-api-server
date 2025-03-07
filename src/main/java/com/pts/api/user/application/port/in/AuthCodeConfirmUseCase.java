package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.AuthCodeConfirmRequestDto;

public interface AuthCodeConfirmUseCase {

    void confirm(AuthCodeConfirmRequestDto dto);
}
