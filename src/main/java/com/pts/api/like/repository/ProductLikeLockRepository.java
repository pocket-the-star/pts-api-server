package com.pts.api.like.repository;

import com.pts.api.global.lock.repository.AbstractLockRepository;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProductLikeLockRepository extends AbstractLockRepository {

    public ProductLikeLockRepository(RedissonClient redisson) {
        super(redisson);
    }

    @Override
    protected String generateKey(Long key) {
        return "product::like::lock::%s".formatted(key);
    }
}
