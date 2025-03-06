package com.pts.api.mail.application.port.in;

public interface AuthMailUseCase {

    void send(String email, String authCode);
}
