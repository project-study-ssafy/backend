package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.comment.domain.repository.CommentRepository;
import com.ssafeople.backend.domain.comment.presentation.dto.request.CommentWriteRequest;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.comment.CommentListEmptyException;
import com.ssafeople.backend.global.exception.comment.CommentNotExistException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import com.ssafeople.backend.global.exception.user.CommentOwnerIsNotCurrentUserException;
import com.ssafeople.backend.global.utils.notify.NotifyUtils;
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

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotifyUtils notifyUtils;

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

    @Override
    public void writeComment(CommentWriteRequest request, User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        Comment comment = new Comment(request.getContent(), post, user);
        commentRepository.save(comment);

        // Post 주인에게 알림을 보낸다.
        notifyUtils.sendNotification(post);
    }

    @Override
    public void deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> CommentNotExistException.EXCEPTION);

        if (comment.getUser() != user) {
            throw CommentOwnerIsNotCurrentUserException.EXCEPTION;
        }

        commentRepository.deleteById(commentId);
    }
}
