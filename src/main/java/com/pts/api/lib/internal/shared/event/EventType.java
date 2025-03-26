package com.pts.api.lib.internal.shared.event;

import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.lib.internal.shared.event.data.OrderCancelData;
import com.pts.api.lib.internal.shared.event.data.OrderCreateData;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    FEED_CREATE(FeedCreateData.class, Topic.FEED_CREATE),
    EMAIL_AUTH(EmailVerifyData.class, Topic.EMAIL_VERIFY),
    PRODUCT_LIKE(ProductLikeData.class, Topic.PRODUCT_LIKE),
    PRODUCT_UNLIKE(ProductUnLikeData.class, Topic.PRODUCT_UNLIKE),
    PRODUCT_CREATE(ProductCreateData.class, Topic.PRODUCT_CREATE),
    ORDER_CREATE(OrderCreateData.class, Topic.ORDER_CREATED),
    ORDER_CANCEL(OrderCancelData.class, Topic.ORDER_CANCELLED);

    private final Class<? extends EventData> dataClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("EventType type={}", type, e);
            return null;
        }
    }

    public static class Topic {

        public static final String EMAIL_VERIFY = "email-verify";
        public static final String FEED_CREATE = "feed-create";
        public static final String PRODUCT_LIKE = "product-like";
        public static final String PRODUCT_UNLIKE = "product-unlike";
        public static final String PRODUCT_CREATE = "product-create";
        public static final String ORDER_CREATED = "order-created";
        public static final String ORDER_CANCELLED = "order-cancelled";
    }
}
