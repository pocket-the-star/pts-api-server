package com.pts.api.product.infrastructure.persistence.adapter;

import com.pts.api.global.lock.repository.AbstractLockRepository;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProductLockRepositoryAdapter extends AbstractLockRepository {

    public ProductLockRepositoryAdapter(RedissonClient redisson) {
        super(redisson);
    }

    @Override
    protected String generateKey(Long key) {
        return "product:" + key;
    }
}
