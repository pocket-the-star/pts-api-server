package com.pts.api.feed.application.service;

import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.ReadFeedUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadFeedApplicationService implements ReadFeedUseCase {

    private final FeedRepositoryPort feedRepository;

    /**
     * 내 피드 조회
     *
     * @param userId     사용자 ID
     * @param lastFeedId 마지막 피드 ID
     * @param limit      조회할 피드 개수
     * @return 피드 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<MyFeedResponse> getMyFeeds(Long userId, Long lastFeedId, Integer limit) {
        return feedRepository.findByUserIdAndDeletedAtIsNull(userId, lastFeedId, limit).stream()
            .map(MyFeedResponse::from)
            .collect(Collectors.toList());
    }

    /**
     * 피드 조회
     *
     * @param lastId 마지막 피드 ID
     * @param limit  조회할 피드 개수
     * @return 피드 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(Long lastId, Integer limit) {
        return feedRepository.findByDeletedAtIsNull(lastId, limit).stream()
            .map(FeedResponse::from)
            .collect(Collectors.toList());
    }
}