package com.pts.api.order.presentation.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.order.application.port.in.CancelOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("CancelOrderController 클래스")
class CancelOrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private CancelOrderUseCase cancelOrderUseCase;

    private static final String BASE_URL = "/api/v1/private/orders";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ORDER_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(cancelOrderUseCase);
    }

    @Nested
    @DisplayName("cancel 메서드 호출 시")
    class DescribeCancel {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            doNothing().when(cancelOrderUseCase).cancel(eq(TEST_USER_ID), eq(TEST_ORDER_ID));

            // When & Then
            ResultActions result = mockMvc.perform(patch(BASE_URL + "/{orderId}", TEST_ORDER_ID)
                .with(user("testUser").roles("USER")));

            result.andExpect(status().isOk());
        }
    }
} 