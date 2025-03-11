package com.pts.api.category.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubCategory {

    private Long id;
    private String name;
    private Long categoryId;

    @Builder
    public SubCategory(Long id, String name, Long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

}
