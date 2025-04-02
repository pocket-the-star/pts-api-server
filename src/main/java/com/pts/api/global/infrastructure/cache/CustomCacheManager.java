package com.pts.api.global.infrastructure.cache;

import com.pts.api.lib.internal.shared.util.serializer.DataSerializer;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomCacheManager {

    private final StringRedisTemplate stringRedisTemplate;
    private final CustomCacheLockRepository customCacheLockProvider;
    private static final String DELIMITER = "::";

    public Object processCache(String prefix, String[] keys, long ttl, Type returnType,
        CustomCacheOriginDataSupplier<?> originDataSupplier) throws Throwable {
        String key = generateKey(prefix, keys);
        String cachedData = stringRedisTemplate.opsForValue().get(key);

        CustomCache cache = null;
        if (cachedData != null) {
            cache = DataSerializer.deserialize(cachedData, CustomCache.class);
        }

        if (cache != null) {
            return cache.parseData(returnType);
        }

        if (!customCacheLockProvider.lock(key)) {
            return originDataSupplier.get();
        }

        try {
            return refresh(originDataSupplier, key, ttl);
        } finally {
            customCacheLockProvider.unlock(key);
        }
    }

    private Object refresh(CustomCacheOriginDataSupplier<?> originDataSupplier, String key,
        long ttl)
        throws Throwable {
        Object result = originDataSupplier.get();

        CustomCache cache = CustomCache.of(result, ttl);

        stringRedisTemplate.opsForValue()
            .set(
                key,
                Objects.requireNonNull(DataSerializer.serialize(cache)),
                Duration.ofSeconds(cache.getTtl())
            );

        return result;
    }

    private String generateKey(String prefix, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder(prefix);
        for (Object arg : args) {
            keyBuilder.append(DELIMITER).append(arg);
        }
        return keyBuilder.toString();
    }
}
