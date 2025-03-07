package com.pts.api.mail.application.port.out;

public interface EmailVerifySenderPort {

    void send(String email, String authCode);
}
