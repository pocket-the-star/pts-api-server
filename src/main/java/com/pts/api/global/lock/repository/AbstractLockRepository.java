package com.pts.api.global.lock.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public abstract class AbstractLockRepository {

    protected final RedissonClient redisson;

    public RLock getLock(Long key) {
        return redisson.getLock(generateKey(key));
    }

    public RLock getFairLock(Long key) {
        return redisson.getFairLock(generateKey(key));
    }

    protected abstract String generateKey(Long key);
}
