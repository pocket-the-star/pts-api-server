package com.pts.api.mail.presentation.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.global.application.event.AuthMailEvent;
import com.pts.api.mail.application.port.in.AuthMailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthMailConsumer {

    private final ObjectMapper objectMapper;
    private final AuthMailUseCase authMailUseCase;

    @KafkaListener(topics = "mail-topic", groupId = "mail-group")
    public void consume(String message) {
        try {
            AuthMailEvent dto = objectMapper.readValue(message, AuthMailEvent.class);
            authMailUseCase.send(dto.email(), dto.authCode());

            log.info("메일 발송 요청 메시지 처리 완료: {}", dto);
        } catch (Exception e) {

            log.error("메일 발송 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }
}
