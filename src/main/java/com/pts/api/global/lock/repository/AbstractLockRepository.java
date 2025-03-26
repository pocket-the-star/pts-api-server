package com.pts.api.global.lock.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public abstract class AbstractLockRepository implements LockRepository {

    protected final RedissonClient redisson;

    @Override
    public RLock getLock(Long key) {
        return redisson.getLock(generateKey(key));
    }

    @Override
    public RLock getFairLock(Long key) {
        return redisson.getFairLock(generateKey(key));
    }

    protected abstract String generateKey(Long key);
}
