package com.pts.api.mail.infrastructure.adapter;

import com.pts.api.mail.application.port.out.EmailVerifySenderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerifySenderAdapterImpl implements EmailVerifySenderPort {

    private final JavaMailSender javaMailSender;
    private static final String SUBJECT = "포켓더스타 인증 코드 안내";
    private static final String TEXT = "인증 코드: %s";

    @Async
    @Override
    public void send(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(SUBJECT);
        message.setText(String.format(TEXT, authCode));

        javaMailSender.send(message);
    }
}