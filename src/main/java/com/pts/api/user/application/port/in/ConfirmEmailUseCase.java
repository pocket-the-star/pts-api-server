package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;

public interface ConfirmEmailUseCase {

    void confirm(AuthCodeConfirmRequest request);
}
