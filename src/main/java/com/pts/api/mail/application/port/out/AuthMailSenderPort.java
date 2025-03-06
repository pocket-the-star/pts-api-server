package com.pts.api.mail.application.port.out;

public interface AuthMailSenderPort {

    void send(String email, String authCode);
}
