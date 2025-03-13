package com.pts.api.user.infrastructure.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.user.domain.model.EmailVerify;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerifyMapper implements IEmailVerifyMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String mapToString(EmailVerify object) {
        Map<String, Object> map = Map.of(
            "email", object.getEmail(),
            "authCode", object.getAuthCode(), 
            "tryCount", object.getTryCount(),
            "isVerified", object.isVerified()
        );

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmailVerify mapToObject(String value) {
        try {
            HashMap<String, Object> map = objectMapper.readValue(value, new TypeReference<HashMap<String, Object>>() {});
            return new EmailVerify(
                (String) map.get("email"), 
                (String) map.get("authCode"),
                ((Integer) map.get("tryCount")).intValue(),
                (Boolean) map.get("isVerified")
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
