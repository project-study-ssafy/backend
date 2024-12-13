package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.comment.domain.repository.CommentRepository;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.global.exception.comment.CommentListEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentListByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        if(commentPage.isEmpty()) {
            throw CommentListEmptyException.EXCEPTION;
        }

        List<CommentResponse> comments = commentPage.getContent().stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .userId(comment.getUser().getId())
                        .nickName(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();

        return new PageImpl<>(comments, pageable, commentPage.getTotalElements());
    }
}
