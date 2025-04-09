package com.pts.api.feed.presentation.controller;

import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드", description = "피드 API")
@RestController
@RequestMapping("/api/v1/private/feeds")
@RequiredArgsConstructor
public class UpdateFeedController {

    private final UpdateFeedUseCase updateFeedUseCase;

    @Operation(summary = "피드 수정", description = "피드를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> update(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long id,
        @Valid @RequestBody UpdateFeedRequest request) {
        updateFeedUseCase.update(userId, id, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}