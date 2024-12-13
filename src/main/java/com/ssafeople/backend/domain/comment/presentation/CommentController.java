package com.ssafeople.backend.domain.comment.presentation;

import com.ssafeople.backend.domain.comment.presentation.dto.request.CommentWriteRequest;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.comment.service.CommentService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.utils.user.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserUtils userUtils;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getPageCommentsList(
            @PathVariable Long postId,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Page<CommentResponse> pagedComments = commentService.getCommentListByPostId(postId, page, size);
        return ResponseEntity.ok(pagedComments);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> writeComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentWriteRequest request
    ) {
        User user = userUtils.getCurrentUser();
        commentService.writeComment(request, user, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        User user = userUtils.getCurrentUser();
        commentService.deleteComment(user, commentId);
        return ResponseEntity.noContent().build();
    }
}
