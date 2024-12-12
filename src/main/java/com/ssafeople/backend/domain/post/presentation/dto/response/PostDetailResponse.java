package com.ssafeople.backend.domain.post.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    private final Short userId;
    private final String nickName;

    private final Short likeCount;
    private final Short commentCount;
    private final Short viewCount;
}
