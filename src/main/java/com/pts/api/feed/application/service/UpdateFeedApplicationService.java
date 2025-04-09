package com.pts.api.feed.application.service;

import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateFeedApplicationService implements UpdateFeedUseCase {

    private final FeedRepositoryPort feedRepository;
    private final DateTimeUtil dateTimeUtil;

    @Override
    @Transactional
    public void update(Long userId, Long id, UpdateFeedRequest request) {
        Feed feed = getFeed(id);
        validateFeedOwnership(feed, userId);

        feed.update(
            request.content(),
            updateFeedImages(request.imageUrls()),
            request.grade(),
            request.price(),
            request.quantity(),
            dateTimeUtil.now()
        );

        feedRepository.save(feed);
    }

    /**
     * 피드 이미지 리스트를 FeedImage 객체 리스트로 변환
     *
     * @param imageUrls 이미지 URL 리스트
     * @return FeedImage 객체 리스트
     */
    private List<FeedImage> updateFeedImages(List<String> imageUrls) {
        return imageUrls.stream()
            .map(url -> FeedImage.builder()
                .url(url)
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 피드 조회
     *
     * @param id 피드 ID
     * @return 피드
     * @throws NotFoundException 피드가 존재하지 않을 경우
     */
    private Feed getFeed(Long id) {
        return feedRepository.findOneById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 피드입니다. id=" + id));
    }

    /**
     * 피드 수정 권한 검증
     *
     * @param feed   피드
     * @param userId 사용자 ID
     * @throws UnAuthorizedException 피드 수정 권한이 없는 경우
     */
    private void validateFeedOwnership(Feed feed, Long userId) {
        if (!feed.getUserId().equals(userId)) {
            throw new UnAuthorizedException(
                "수정 권한이 없는 피드입니다. feedId: " + feed.getId() + ", userId: " + userId);
        }
    }
}
