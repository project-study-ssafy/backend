package com.ssafeople.backend.domain.comment.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;

    private final Short userId;
    private final String nickName;
}
