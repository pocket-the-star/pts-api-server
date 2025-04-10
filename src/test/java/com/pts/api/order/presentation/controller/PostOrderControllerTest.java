package com.pts.api.order.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.CreateShippingRequest;
import com.pts.api.order.application.port.in.PostOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("OrderController 클래스")
class PostOrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private PostOrderUseCase postOrderUseCase;

    private static final String BASE_URL = "/api/v1/private/orders";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(postOrderUseCase);
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            CreateOrderRequest request = new CreateOrderRequest(
                TEST_FEED_ID,
                1,
                new CreateShippingRequest(
                    "홍길동",
                    "서울시 강남구",
                    "123-45",
                    "12345",
                    "일반배송"
                )
            );

            doNothing().when(postOrderUseCase)
                .create(eq(TEST_USER_ID), any(CreateOrderRequest.class));

            // When & Then
            ResultActions result = mockMvc.perform(post(BASE_URL)
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            result.andExpect(status().isOk());
        }
    }
} 