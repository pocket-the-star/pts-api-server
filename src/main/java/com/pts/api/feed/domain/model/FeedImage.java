package com.pts.api.feed.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedImage extends BaseModel {

    private Long id;
    private String url;
} 