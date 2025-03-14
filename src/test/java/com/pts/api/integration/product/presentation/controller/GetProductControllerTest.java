package com.pts.api.integration.product.presentation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.product.application.dto.response.GetProductResponseDto;
import com.pts.api.product.application.port.in.GetProductUseCase;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("GetProductController 클래스")
public class GetProductControllerTest extends BaseIntegrationTest {

    private static final String PRODUCTS_URL = "/api/v1/products";

    @Autowired
    private GetProductUseCase getProductUseCase;

    @Nested
    @DisplayName("getProducts 메서드 호출 시")
    class DescribeGetProducts {

        @Nested
        @DisplayName("필터 없이 요청하는 경우")
        class ContextWithoutFilters {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                // Given
                GetProductResponseDto sampleProduct = new GetProductResponseDto(
                    1L,
                    "Test Product",
                    "http://example.com/image.jpg",
                    10L,
                    20L,
                    30L,
                    100L,
                    200L,
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusHours(1)
                );
                List<GetProductResponseDto> productList = Collections.singletonList(sampleProduct);
                when(getProductUseCase.getProducts(null, null, null, 0))
                    .thenReturn(productList);

                // When & Then
                mockMvc.perform(get(PRODUCTS_URL)
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("필터와 offset 파라미터가 포함된 경우")
        class ContextWithFilters {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                // Given
                Long groupId = 1L;
                Long categoryId = 2L;
                Long subCategoryId = 3L;
                int offset = 0;
                GetProductResponseDto sampleProduct = new GetProductResponseDto(
                    2L,
                    "Filtered Product",
                    "http://example.com/filtered.jpg",
                    groupId,
                    categoryId,
                    subCategoryId,
                    150L,
                    250L,
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusHours(1)
                );
                List<GetProductResponseDto> productList = Collections.singletonList(sampleProduct);
                when(getProductUseCase.getProducts(groupId, categoryId, subCategoryId, offset))
                    .thenReturn(productList);

                // When & Then
                mockMvc.perform(get(PRODUCTS_URL)
                        .param("groupId", groupId.toString())
                        .param("categoryId", categoryId.toString())
                        .param("subCategoryId", subCategoryId.toString())
                        .param("offset", String.valueOf(offset))
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }
}