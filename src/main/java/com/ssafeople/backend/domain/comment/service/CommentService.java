package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    Page<CommentResponse> getCommentListByPostId(Short boardId, Long postId, int page, int size);
}
