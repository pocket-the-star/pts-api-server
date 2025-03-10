package com.pts.api.lib.internal.shared.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS(1), REFRESH(2);

    private final int value;

    TokenType(int value) {
        this.value = value;
    }

    public static TokenType valueOf(int value) {
        for (TokenType type : TokenType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
