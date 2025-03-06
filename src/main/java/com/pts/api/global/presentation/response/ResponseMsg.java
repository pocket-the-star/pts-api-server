package com.pts.api.global.presentation.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMsg {
    OK("OK");

    private final String value;
} 