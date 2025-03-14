package com.pts.api.category.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("GetCategoryController 클래스")
public class CategoryControllerTest extends BaseIntegrationTest {

    private static final String CATEGORIES_URL = "/api/v1/categories";

    @Nested
    @DisplayName("getCategories 메서드 호출 시")
    class DescribeGetCategories {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                mockMvc.perform(get(CATEGORIES_URL)
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("getCategory 메서드 호출 시")
    class DescribeGetCategory {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                Long categoryId = 1L;
                mockMvc.perform(get(CATEGORIES_URL + "/{categoryId}", categoryId)
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }
}


