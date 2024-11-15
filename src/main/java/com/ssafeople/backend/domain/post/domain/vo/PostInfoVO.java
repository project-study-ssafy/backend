package com.ssafeople.backend.domain.post.domain.vo;

import com.ssafeople.backend.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostInfoVO {
    private final Long id;
    private final Short userId;
    private final Short boardId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<Comment> comments;
}
