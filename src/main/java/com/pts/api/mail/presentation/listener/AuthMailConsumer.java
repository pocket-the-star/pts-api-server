package com.pts.api.mail.presentation.listener;

import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType.Topic;
import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.mail.application.port.in.AuthMailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthMailConsumer {

    private final AuthMailUseCase authMailUseCase;

    @KafkaListener(topics = {
        Topic.EMAIL_VERIFY
    }, groupId = "mail-group")
    public void consume(String message) {
        try {
            EmailVerifyData emailVerifyData = (EmailVerifyData) Event.fromJson(message).getData();
            authMailUseCase.send(emailVerifyData.email(), emailVerifyData.authCode());

            log.info("메일 발송 요청 메시지 처리 완료: {}", message);
        } catch (Exception e) {

            log.error("메일 발송 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }
}
