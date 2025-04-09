package com.pts.api.feed.presentation.controller;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.feed.application.port.in.PostFeedUseCase;
import com.pts.api.feed.application.port.in.ReadFeedListUseCase;
import com.pts.api.feed.application.port.in.ReadMyFeedUseCase;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
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
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final PostFeedUseCase postFeedUseCase;
    private final ReadFeedListUseCase readFeedListUseCase;
    private final UpdateFeedUseCase updateFeedUseCase;
    private final DeleteFeedUseCase deleteFeedUseCase;
    private final ReadMyFeedUseCase readMyFeedUseCase;

    @Operation(summary = "피드 생성", description = "피드를 생성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateFeedRequest request) {
        postFeedUseCase.create(userId, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<FeedResponse>>> findById(@RequestParam Long lastFeedId,
        @RequestParam Integer limit) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readFeedListUseCase.findAll(lastFeedId, limit));
    }

    @Operation(summary = "피드 수정", description = "피드를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> update(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long id,
        @Valid @RequestBody UpdateFeedRequest request) {
        updateFeedUseCase.update(userId, id, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@AuthenticationPrincipal Long userId,
        @PathVariable Long id) {
        deleteFeedUseCase.delete(userId, id);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "내 피드 목록 조회", description = "내 피드 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<BaseResponse<List<MyFeedResponse>>> findByUserId(
        @AuthenticationPrincipal Long userId, @RequestParam(required = false) Long lastFeedId,
        @RequestParam(required = false) Integer limit) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readMyFeedUseCase.findByUserId(userId, lastFeedId, limit));
    }
} 