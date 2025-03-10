package com.pts.api.user.infrastructure.event.adapter;

import com.pts.api.lib.internal.shared.event.EmailVerifyEvent;
import com.pts.api.user.application.port.out.EmailVerifyProducerPort;
import com.pts.api.user.infrastructure.mapper.IEmailVerifyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerifyProducer implements EmailVerifyProducerPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final IEmailVerifyMapper<EmailVerifyEvent> emailVerifyMapper;
    @Value("${kafka.topic.email-verify}")
    private String topic;

    @Override
    public void emailVerify(String email, String authCode) {

        kafkaTemplate.send(topic,
            emailVerifyMapper.mapToString(new EmailVerifyEvent(email, authCode)));
    }
}
