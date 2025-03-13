package com.pts.api.global.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "기본 응답 데이터 구조")
public record BaseResponse<T>(
    @Schema(description = "결과 메시지", example = "OK")
    String resultMsg,
    @Schema(description = "결과 코드 (예: 200은 성공, 400은 클라이언트 오류)", example = "200")
    int resultCode,
    @Schema(description = "반환 데이터")
    T data
) {

}