package com.pts.api.feed.controller;

import com.pts.api.feed.dto.request.CreateFeedRequestDto;
import com.pts.api.feed.dto.request.UpdateFeedRequestDto;
import com.pts.api.feed.dto.response.ReadFeedResponseDto;
import com.pts.api.feed.dto.response.ReadMyFeedResponseDto;
import com.pts.api.feed.service.FeedService;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드", description = "피드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 작성", description = "피드를 생성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> post(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateFeedRequestDto request) {

        feedService.create(userId, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "피드 수정", description = "피드를 수정합니다.")
    @PutMapping("/{feedId}")
    public ResponseEntity<BaseResponse<Void>> put(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long feedId,
        @Valid @RequestBody UpdateFeedRequestDto request) {

        feedService.update(userId, feedId, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @DeleteMapping("/{feedId}")
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long feedId) {

        feedService.delete(userId, feedId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "내 피드 조회", description = "내가 작성한 피드를 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<BaseResponse<List<ReadMyFeedResponseDto>>> getMyFeed(
        @AuthenticationPrincipal Long userId, @RequestParam Long offset,
        @RequestParam Integer limit) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            feedService.getMyFeeds(userId, offset, limit));
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<ReadFeedResponseDto>>> getFeed(
        @RequestParam Long offset, @RequestParam Integer limit) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            feedService.getFeeds(offset, limit));
    }
}
