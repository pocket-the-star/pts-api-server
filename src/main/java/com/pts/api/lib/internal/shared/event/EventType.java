package com.pts.api.lib.internal.shared.event;

import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    EMAIL_AUTH(EmailVerifyData.class, Topic.EMAIL_VERIFY);

    private final Class<? extends EventData> dataClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("EventType type={}", type, e);
            return null;
        }
    }

    public static class Topic {

        public static final String EMAIL_VERIFY = "email-verify";
    }
}
