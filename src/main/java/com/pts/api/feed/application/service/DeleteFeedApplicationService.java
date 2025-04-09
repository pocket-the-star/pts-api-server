package com.pts.api.feed.application.service;

import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteFeedApplicationService implements DeleteFeedUseCase {

    private final FeedRepositoryPort feedRepository;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 피드 삭제
     *
     * @param userId 사용자 ID
     * @param id     피드 ID
     * @throws NotFoundException     피드가 존재하지 않을 경우
     * @throws UnAuthorizedException 피드 삭제 권한이 없는 경우
     */
    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        Feed feed = getFeed(id);
        validateFeedOwnership(feed, userId);
        feed.delete(dateTimeUtil.now());
        feedRepository.save(feed);
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
     * 피드 삭제 권한 검증
     *
     * @param feed   피드
     * @param userId 사용자 ID
     * @throws UnAuthorizedException 피드 삭제 권한이 없는 경우
     */
    private void validateFeedOwnership(Feed feed, Long userId) {
        if (!feed.getUserId().equals(userId)) {
            throw new UnAuthorizedException(
                "삭제 권한이 없는 피드입니다. feedId: " + feed.getId() + ", userId: " + userId);
        }
    }
}
