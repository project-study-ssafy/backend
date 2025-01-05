package com.ssafeople.backend.domain.post.presentation.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostSummaryResponse {
    private final Long id;
    private final String nickName;
    private final String title;
    private final LocalDateTime createdAt;

    private final Short likeCount;
    private final Short commentCount;
    private final Short viewCount;

    private final String imageUrl;
}
