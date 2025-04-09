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

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        Feed feed = getFeed(id);
        validateFeedOwnership(feed, userId);
        feed.delete(dateTimeUtil.now());
        feedRepository.save(feed);
    }

    private Feed getFeed(Long id) {
        return feedRepository.findOneById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 피드입니다. id=" + id));
    }

    private void validateFeedOwnership(Feed feed, Long userId) {
        if (!feed.getUserId().equals(userId)) {
            throw new UnAuthorizedException(
                "삭제 권한이 없는 피드입니다. feedId: " + feed.getId() + ", userId: " + userId);
        }
    }
}
