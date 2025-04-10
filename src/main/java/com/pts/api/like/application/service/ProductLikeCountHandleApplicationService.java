package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.like.application.port.in.ProductLikeCountHandleUseCase;
import com.pts.api.like.application.service.handler.ProductLikeCountEventHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductLikeCountHandleApplicationService implements ProductLikeCountHandleUseCase {

    private final List<ProductLikeCountEventHandler> eventHandlers;

    /**
     * 이벤트를 처리합니다.
     *
     * @param event 처리할 이벤트
     */
    @Override
    public void handleEvent(Event event) {
        eventHandlers.stream()
            .filter(handler -> handler.supports(event))
            .findFirst()
            .ifPresentOrElse(
                handler -> handler.handle(event),
                () -> {
                    throw new NotFoundException("존재하지 않는 이벤트입니다.: " + event);
                });
    }
}
