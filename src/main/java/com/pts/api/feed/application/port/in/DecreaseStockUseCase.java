package com.pts.api.feed.application.port.in;

public interface DecreaseStockUseCase {

    void decreaseStock(Long productId, Integer quantity);
}
