package com.pts.api.user.producer;

import com.pts.api.lib.internal.shared.event.EmailVerifyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class EmailVerifyProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.email-verify}")
    private String topic;

    public void send(String email, String authCode) {
        try {
            EmailVerifyEvent event = new EmailVerifyEvent(email, authCode);
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize EmailVerifyEvent", e);
        }
    }
} 