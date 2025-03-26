package com.pts.api.product.application.port.in;

public interface PriceUpdateUseCase {

    void updatePrice(Long productId, Integer price);
}
