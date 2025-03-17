package com.pts.api.product.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.product.dto.response.GetProductResponseDto;
import com.pts.api.product.service.QProductService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("GetProductController 클래스")
public class QProductControllerTest extends BaseIntegrationTest {

    private static final String PRODUCTS_URL = "/api/v1/products";

    @Autowired
    private QProductService QProductService;

    @Nested
    @DisplayName("getProducts 메서드 호출 시")
    class DescribeGetProducts {

        LocalDateTime NOW = LocalDateTime.now();

        @Nested
        @DisplayName("필터 없이 요청하는 경우")
        class ContextWithoutFilters {


            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                // Given
                GetProductResponseDto sampleProduct = new GetProductResponseDto(
                    101L, "Product1", "http://image1.jpg",
                    100L, 200L, NOW, NOW
                );
                List<GetProductResponseDto> productList = Collections.singletonList(sampleProduct);
                when(QProductService.getProducts(null, null, null, 0))
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
                    101L, "Product1", "http://image1.jpg",
                    100L, 200L, NOW, NOW
                );
                List<GetProductResponseDto> productList = Collections.singletonList(sampleProduct);
                when(QProductService.getProducts(groupId, categoryId, subCategoryId, offset))
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