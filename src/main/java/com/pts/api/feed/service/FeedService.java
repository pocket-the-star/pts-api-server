package com.pts.api.feed.service;

import com.pts.api.feed.dto.request.CreateFeedImageRequestDto;
import com.pts.api.feed.dto.request.CreateFeedRequestDto;
import com.pts.api.feed.exception.FeedAlreadyExistsException;
import com.pts.api.feed.model.Feed;
import com.pts.api.feed.model.FeedImage;
import com.pts.api.feed.repository.FeedRepository;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final ProductRepository productRepository;
    private final OutboxPublisher outboxPublisher;

    @Transactional
    public void create(Long userId, CreateFeedRequestDto dto) {
        productRepository.findById(dto.productId())
            .orElseThrow(
                () -> new NotFoundException("존재하지 않는 상품입니다. productId: " + dto.productId()));

        Optional<Feed> feed = feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
            userId, dto.productId(), FeedStatus.PENDING, dto.feedType());

        if (feed.isPresent()) {
            throw new FeedAlreadyExistsException("이미 등록된 피드가 존재합니다. userId: " + userId
                + ", productId: " + dto.productId());
        }

        Feed newFeed = feedRepository.save(mapToFeed(userId, dto));
        outboxPublisher.publish(
            EventType.FEED_CREATE,
            new FeedCreateData(
                newFeed.getUserId(),
                newFeed.getProductId(),
                newFeed.getFeedImages().get(0).getImageUrl(),
                newFeed.getType(),
                newFeed.getGrade(),
                newFeed.getPrice(),
                newFeed.getQuantity()
            )
        );
    }

    private List<FeedImage> mapToFeedImages(List<CreateFeedImageRequestDto> imageUrls) {
        return IntStream.range(0, imageUrls.size())
            .mapToObj(i -> FeedImage.builder()
                .imageUrl(imageUrls.get(i).imageUrl())
                .sortOrder(i)
                .build())
            .toList();
    }

    private Feed mapToFeed(Long userId, CreateFeedRequestDto dto) {
        return Feed.builder()
            .userId(userId)
            .productId(dto.productId())
            .content(dto.content())
            .feedImages(mapToFeedImages(dto.feedImages()))
            .feedType(dto.feedType())
            .grade(dto.grade())
            .price(dto.price())
            .quantity(dto.quantity())
            .status(FeedStatus.PENDING)
            .build();
    }
}
