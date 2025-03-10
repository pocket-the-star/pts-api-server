package com.pts.api.lib.internal.shared.util.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtil implements IDateTimeUtil {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    @Override
    public LocalDateTime now() {
        return ZonedDateTime.now(KST_ZONE).toLocalDateTime().withNano(0);
    }
}
