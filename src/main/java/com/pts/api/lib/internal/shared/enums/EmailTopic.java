package com.pts.api.lib.internal.shared.enums;

public enum EmailTopic {
    VERIFY_EMAIL("Verify-Email");

    private final String topic;

    EmailTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public static EmailTopic fromString(String text) {
        for (EmailTopic b : EmailTopic.values()) {
            if (b.topic.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
