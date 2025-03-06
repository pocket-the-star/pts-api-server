package com.pts.api.global.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
    String resultMsg,
    int resultCode,
    T data
) {

}