package com.pts.api.feed.application.service;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.port.in.PostFeedUseCase;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostFeedApplicationService implements PostFeedUseCase {

    private final FeedRepositoryPort feedRepository;
    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort eventPublisher;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 피드 등록
     *
     * @param userId  사용자 ID
     * @param request 피드 등록 요청
     * @throws NotFoundException        상품이 존재하지 않을 경우
     * @throws AlreadyExistsException   이미 등록된 피드가 존재할 경우
     * @throws IllegalArgumentException 피드 등록 요청이 유효하지 않을 경우
     */
    @Override
    @Transactional
    public void create(Long userId, CreateFeedRequest request) {
        verifyProductExists(request.productId());
        checkDuplicateFeed(userId, request.productId());

        List<FeedImage> images = mapFeedImages(request);

        LocalDateTime now = dateTimeUtil.now();
        Feed newFeed = buildFeed(userId, request, images, now);
        Feed savedFeed = feedRepository.save(newFeed);

        publishFeedCreateEvent(savedFeed);
    }

    /**
     * 상품 존재 여부 확인
     *
     * @param productId 상품 ID
     * @throws NotFoundException 상품이 존재하지 않을 경우
     */
    private void verifyProductExists(Long productId) {
        productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다. productId: " + productId));
    }


    /**
     * 중복 피드 확인
     *
     * @param userId    사용자 ID
     * @param productId 상품 ID
     * @throws AlreadyExistsException 이미 등록된 피드가 존재할 경우
     */
    private void checkDuplicateFeed(Long userId, Long productId) {
        Optional<Feed> feed = feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
            userId, productId, FeedStatus.PENDING);
        if (feed.isPresent()) {
            throw new AlreadyExistsException(
                "이미 등록된 피드가 존재합니다. userId: " + userId + ", productId: " + productId);
        }
    }

    /**
     * 피드 이미지 URL 리스트를 FeedImage 객체 리스트로 변환
     *
     * @param request 피드 등록 요청
     * @return FeedImage 객체 리스트
     */
    private List<FeedImage> mapFeedImages(CreateFeedRequest request) {
        return request.imageUrls().stream()
            .map(this::createFeedImage)
            .toList();
    }

    /**
     * FeedImage 객체 생성
     *
     * @param url 이미지 URL
     * @return FeedImage 객체
     */
    private FeedImage createFeedImage(String url) {
        return FeedImage.builder()
            .url(url)
            .build();
    }

    /**
     * 피드 객체 생성
     *
     * @param userId  사용자 ID
     * @param request 피드 등록 요청
     * @param images  FeedImage 객체 리스트
     * @param now     현재 시간
     * @return Feed 객체
     */
    private Feed buildFeed(Long userId, CreateFeedRequest request, List<FeedImage> images,
        LocalDateTime now) {
        return Feed.builder()
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
    }

    /**
     * 피드 생성 이벤트 발행
     *
     * @param savedFeed 저장된 피드
     */
    private void publishFeedCreateEvent(Feed savedFeed) {
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
}
