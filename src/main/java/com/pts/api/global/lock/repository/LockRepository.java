package com.pts.api.global.lock.repository;

import org.redisson.api.RLock;

public interface LockRepository {

    public RLock getLock(Long key);

    public RLock getFairLock(Long key);
}
