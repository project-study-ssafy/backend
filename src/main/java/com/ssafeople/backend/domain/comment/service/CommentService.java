package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.comment.presentation.dto.request.CommentWriteRequest;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.user.domain.User;
import org.springframework.data.domain.Page;

public interface CommentService {
    Page<CommentResponse> getCommentListByPostId(Long postId, int page, int size);
    void writeComment(CommentWriteRequest request, User user, Long postId);
    void deleteComment(User user, Long commentId);
}
