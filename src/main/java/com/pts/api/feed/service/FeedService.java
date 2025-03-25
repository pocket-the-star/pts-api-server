package com.pts.api.feed.service;

import com.pts.api.feed.dto.request.CreateFeedRequestDto;
import com.pts.api.feed.dto.request.UpdateFeedRequestDto;
import com.pts.api.feed.dto.response.ReadFeedResponseDto;
import com.pts.api.feed.exception.FeedAlreadyExistsException;
import com.pts.api.feed.model.Feed;
import com.pts.api.feed.repository.FeedImageRepository;
import com.pts.api.feed.repository.FeedRepository;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final ProductRepository productRepository;
    private final OutboxPublisher outboxPublisher;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public Feed create(Long userId, CreateFeedRequestDto dto) {
        productRepository.findById(dto.productId()).orElseThrow(
            () -> new NotFoundException("존재하지 않는 상품입니다. productId: " + dto.productId()));

        Optional<Feed> feed = feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
            userId, dto.productId(), FeedStatus.PENDING, dto.feedType());

        if (feed.isPresent()) {
            throw new FeedAlreadyExistsException(
                "이미 등록된 피드가 존재합니다. userId: " + userId + ", productId: " + dto.productId());
        }

        Feed newFeed = feedRepository.save(dto.toFeed(userId, dateTimeUtil.now()));
        outboxPublisher.publish(EventType.FEED_CREATE,
            new FeedCreateData(newFeed.getUserId(), newFeed.getProductId(),
                newFeed.getFeedImages().isEmpty() ? null
                    : newFeed.getFeedImages().get(0).getImageUrl(), newFeed.getType(),
                newFeed.getGrade(), newFeed.getPrice(), newFeed.getQuantity()));

        return newFeed;
    }

    @Transactional
    public Feed update(Long userId, Long feedId, UpdateFeedRequestDto dto) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 피드입니다. feedId: " + feedId));

        if (!feed.getUserId().equals(userId)) {
            throw new UnauthorizedException(
                "수정 권한이 없는 피드입니다. feedId: " + feedId + ", userId: " + userId);
        }

        return feedRepository.save(dto.toFeed(feed, dateTimeUtil.now()));
    }

    @Transactional
    public void delete(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 피드입니다. feedId: " + feedId));

        if (!feed.getUserId().equals(userId)) {
            throw new UnauthorizedException(
                "삭제 권한이 없는 피드입니다. feedId: " + feedId + ", userId: " + userId);
        }
        feed.setDeletedAt(dateTimeUtil.now());

        feedRepository.delete(feed);
    }

    @Transactional(readOnly = true)
    public List<ReadFeedResponseDto> getMyFeeds(Long userId, Long offset, Integer limit) {
        return feedRepository.findByUserIdAndDeletedAtIsNull(userId, offset, limit).stream().map(
            ReadFeedResponseDto::fromFeed).toList();
    }
}
