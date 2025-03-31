package com.pts.api.feed.infrastructure.persistence.adapter;

import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.infrastructure.persistence.entity.FeedEntity;
import com.pts.api.feed.infrastructure.persistence.repository.FeedRepository;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryAdapter implements FeedRepositoryPort {

    private final FeedRepository feedRepository;

    @Override
    public Feed save(Feed feed) {
        return feedRepository.save(FeedEntity.fromModel(feed)).toModel();
    }

    @Override
    public Optional<Feed> findByUserIdAndProductIdAndFeedStatusAndFeedType(Long userId,
        Long ProductId, FeedStatus status) {
        return feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(userId, ProductId,
                status)
            .map(FeedEntity::toModel);
    }

    @Override
    public List<Feed> findByUserIdAndDeletedAtIsNull(Long userId, Long offset, Integer limit) {
        return feedRepository.findByUserIdAndDeletedAtIsNull(userId, offset, limit).stream()
            .map(FeedEntity::toModel)
            .toList();
    }

    @Override
    public List<Feed> findByDeletedAtIsNull(Long offset, Integer limit) {
        return feedRepository.findByDeletedAtIsNull(offset, limit).stream()
            .map(FeedEntity::toModel)
            .toList();
    }

    @Override
    public Optional<Feed> findOneById(Long id) {
        return feedRepository.findOneById(id).map(FeedEntity::toModel);
    }
}