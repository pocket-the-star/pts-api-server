package com.pts.api.order.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.CreateShippingRequest;
import com.pts.api.order.application.dto.request.UpdateOrderRequest;
import com.pts.api.order.application.port.in.CreateOrderUseCase;
import com.pts.api.order.application.port.in.UpdateOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("OrderController 클래스")
class OrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private UpdateOrderUseCase updateOrderUseCase;

    private static final String BASE_URL = "/api/v1/orders";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ORDER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(createOrderUseCase, updateOrderUseCase);
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

            doNothing().when(createOrderUseCase)
                .create(eq(TEST_USER_ID), any(CreateOrderRequest.class));

            // When & Then
            ResultActions result = mockMvc.perform(post(BASE_URL)
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("updateStatus 메서드 호출 시")
    class DescribeUpdateStatus {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            UpdateOrderRequest request = new UpdateOrderRequest(OrderStatus.PROCESSING);

            doNothing().when(updateOrderUseCase)
                .updateStatus(eq(TEST_USER_ID), eq(TEST_ORDER_ID), any(UpdateOrderRequest.class));

            // When & Then
            ResultActions result = mockMvc.perform(put(BASE_URL + "/{orderId}", TEST_ORDER_ID)
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("cancel 메서드 호출 시")
    class DescribeCancel {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            doNothing().when(updateOrderUseCase).cancel(eq(TEST_USER_ID), eq(TEST_ORDER_ID));

            // When & Then
            ResultActions result = mockMvc.perform(patch(BASE_URL + "/{orderId}", TEST_ORDER_ID)
                .with(user("testUser").roles("USER")));

            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("delete 메서드 호출 시")
    class DescribeDelete {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            doNothing().when(updateOrderUseCase).delete(eq(TEST_USER_ID), eq(TEST_ORDER_ID));

            // When & Then
            ResultActions result = mockMvc.perform(delete(BASE_URL + "/{orderId}", TEST_ORDER_ID)
                .with(user("testUser").roles("USER")));

            result.andExpect(status().isOk());
        }
    }
} 