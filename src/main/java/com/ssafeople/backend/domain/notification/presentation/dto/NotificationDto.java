package com.ssafeople.backend.domain.notification.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationDto {
    private Long id;
    private String content;
    private short userId; // 사용자 ID
    private Long postId; // 게시물 ID
    private boolean isRead;
}
