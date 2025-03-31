package com.pts.api.feed.application.service;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.CreateFeedUseCase;
import com.pts.api.feed.application.port.in.DecreaseStockUseCase;
import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.feed.application.port.in.ReadFeedListUseCase;
import com.pts.api.feed.application.port.in.ReadMyFeedUseCase;
import com.pts.api.feed.application.port.in.RestoreStockUseCase;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.common.exception.FeedAlreadyExistsException;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedApplicationService implements CreateFeedUseCase, ReadFeedListUseCase,
    UpdateFeedUseCase, DeleteFeedUseCase, ReadMyFeedUseCase, DecreaseStockUseCase,
    RestoreStockUseCase {

    private final FeedRepositoryPort feedRepository;
    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort eventPublisher;
    private final DateTimeUtil dateTimeUtil;

    @Override
    @Transactional
    public void create(Long userId, CreateFeedRequest request) {
        productRepository.findById(request.productId())
            .orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다. productId: " + request.productId()));
        Optional<Feed> feed = feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
            userId, request.productId(), FeedStatus.PENDING);

        if (feed.isPresent()) {
            throw new FeedAlreadyExistsException(
                "이미 등록된 피드가 존재합니다. userId: " + userId + ", productId: " + request.productId());
        }

        List<FeedImage> images = request.imageUrls().stream()
            .map(url -> FeedImage.builder()
                .url(url)
                .build())
            .toList();
        LocalDateTime now = dateTimeUtil.now();

        Feed newFeed = Feed.builder()
            .userId(userId)
            .productId(request.productId())
            .content(request.content())
            .feedImages(images)
            .grade(request.grade())
            .price(request.price())
            .quantity(request.quantity())
            .createdAt(now)
            .updatedAt(now)
            .status(FeedStatus.PENDING)
            .deletedAt(null)
            .build();

        Feed savedFeed = feedRepository.save(newFeed);

        eventPublisher.publish(
            EventType.FEED_CREATE,
            new FeedCreateData(
                savedFeed.getUserId(),
                savedFeed.getProductId(),
                savedFeed.getFeedImages().isEmpty() ? null
                    : savedFeed.getFeedImages().get(0).getUrl(),
                savedFeed.getGrade(),
                savedFeed.getPrice(),
                savedFeed.getQuantity()
            )
        );
    }

    private Feed getFeed(Long id) {
        return feedRepository.findOneById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 피드입니다. id=" + id));
    }

    @Override
    @Transactional
    public void update(Long userId, Long id, UpdateFeedRequest request) {
        Feed feed = getFeed(id);

        if (!feed.getUserId().equals(userId)) {
            throw new UnauthorizedException(
                "수정 권한이 없는 피드입니다. feedId: " + feed.getId() + ", userId: " + userId);
        }

        List<FeedImage> images = request.imageUrls().stream()
            .map(url -> FeedImage.builder()
                .url(url)
                .build())
            .collect(Collectors.toList());

        feed.update(
            request.content(),
            images,
            request.grade(),
            request.price(),
            request.quantity(),
            dateTimeUtil.now()
        );
    }

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        Feed feed = getFeed(id);
        if (!feed.getUserId().equals(userId)) {
            throw new UnauthorizedException(
                "삭제 권한이 없는 피드입니다. feedId: " + feed.getId() + ", userId: " + userId);
        }

        feed.delete(dateTimeUtil.now());
        feedRepository.save(feed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyFeedResponse> findByUserId(Long userId, Long lastFeedId, Integer limit) {
        return feedRepository.findByUserIdAndDeletedAtIsNull(userId, lastFeedId, limit).stream()
            .map(MyFeedResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> findAll(Long lastId, Integer limit) {
        return feedRepository.findByDeletedAtIsNull(lastId, limit).stream()
            .map(FeedResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void decreaseStock(Long feedId, Integer quantity) {
        Feed feed = getFeed(feedId);

        feed.decreaseStock(quantity, dateTimeUtil.now());

        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void restoreStock(Long feedId, Integer quantity) {
        Feed feed = getFeed(feedId);

        feed.restoreStock(quantity, dateTimeUtil.now());

        feedRepository.save(feed);
    }
}