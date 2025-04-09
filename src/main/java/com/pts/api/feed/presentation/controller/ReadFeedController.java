package com.pts.api.feed.presentation.controller;

import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.ReadFeedUseCase;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드", description = "피드 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReadFeedController {

    private final ReadFeedUseCase readFeedUseCase;

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @GetMapping("/feeds")
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findById(@RequestParam Long lastFeedId,
        @RequestParam Integer limit) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readFeedUseCase.getFeeds(lastFeedId, limit));
    }

    @Operation(summary = "내 피드 목록 조회", description = "내 피드 목록을 조회합니다.")
    @GetMapping("/private/feeds/my")
    public ResponseEntity<BaseResponse<List<MyFeedResponse>>> findByUserId(
        @AuthenticationPrincipal Long userId, @RequestParam(required = false) Long lastFeedId,
        @RequestParam(required = false) Integer limit) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readFeedUseCase.getMyFeeds(userId, lastFeedId, limit));
    }
} 