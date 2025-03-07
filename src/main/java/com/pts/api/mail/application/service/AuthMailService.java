package com.pts.api.mail.application.service;

import com.pts.api.mail.application.port.in.AuthMailUseCase;
import com.pts.api.mail.application.port.out.EmailVerifySenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMailService implements AuthMailUseCase {

    private final EmailVerifySenderPort emailVerifySenderPort;

    @Override
    public void send(String email, String authCode) {
        emailVerifySenderPort.send(email, authCode);
    }
}
