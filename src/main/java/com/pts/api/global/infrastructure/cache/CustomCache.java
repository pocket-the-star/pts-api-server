package com.pts.api.global.infrastructure.cache;

import com.pts.api.lib.internal.shared.util.serializer.DataSerializer;
import java.lang.reflect.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomCache {

    private String data;
    private long ttl;

    public static CustomCache of(Object data, long ttl) {
        return new CustomCache(DataSerializer.serialize(data), ttl);
    }

    public <T> T parseData(Type dataType) {
        return DataSerializer.deserialize(data, dataType);
    }
}
