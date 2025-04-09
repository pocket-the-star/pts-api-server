package com.pts.api.feed.presentation.controller;

import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드", description = "피드 API")
@RestController
@RequestMapping("/api/v1/private/feeds")
@RequiredArgsConstructor
public class DeleteFeedController {

    private final DeleteFeedUseCase deleteFeedUseCase;

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@AuthenticationPrincipal Long userId,
        @PathVariable Long id) {
        deleteFeedUseCase.delete(userId, id);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}