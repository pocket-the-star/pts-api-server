package com.pts.api.product.repository;

import com.pts.api.global.lock.repository.AbstractLockRepository;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProductLockRepository extends AbstractLockRepository {

    public ProductLockRepository(RedissonClient redisson) {
        super(redisson);
    }

    @Override
    protected String generateKey(Long key) {
        return "product:" + key;
    }
}
