package com.pts.api.user.application.port.out;

public interface EmailVerifyProducerPort {

    void emailVerify(String email, String authCode);
}
