package com.pts.api.feed.presentation.controller;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.port.in.PostFeedUseCase;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드", description = "피드 API")
@RestController
@RequestMapping("/api/v1/private/feeds")
@RequiredArgsConstructor
public class PostFeedController {

    private final PostFeedUseCase postFeedUseCase;

    @Operation(summary = "피드 생성", description = "피드를 생성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateFeedRequest request) {
        postFeedUseCase.create(userId, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}