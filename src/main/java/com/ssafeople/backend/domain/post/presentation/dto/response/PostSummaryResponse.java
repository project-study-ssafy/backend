package com.ssafeople.backend.domain.post.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSummaryResponse {
    private final Long id;
    private final String userName;
    private final String title;
    private final LocalDateTime createdAt;
}
