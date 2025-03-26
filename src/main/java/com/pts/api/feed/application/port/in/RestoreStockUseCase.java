package com.pts.api.feed.application.port.in;

public interface RestoreStockUseCase {

    void restoreStock(Long productId, Integer quantity);

}
