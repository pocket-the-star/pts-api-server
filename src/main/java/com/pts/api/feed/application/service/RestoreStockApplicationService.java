package com.pts.api.feed.application.service;

import com.pts.api.feed.application.port.in.RestoreStockUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestoreStockApplicationService implements RestoreStockUseCase {

    private final FeedRepositoryPort feedRepository;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 피드 재고 복원
     *
     * @param feedId   피드 ID
     * @param quantity 재고 수량
     * @throws NotFoundException 피드가 존재하지 않을 경우
     */
    @Override
    @Transactional
    public void restoreStock(Long feedId, Integer quantity) {
        Feed feed = getFeed(feedId);

        feed.restoreStock(quantity, dateTimeUtil.now());

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
}
