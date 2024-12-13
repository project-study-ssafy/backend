package com.ssafeople.backend.domain.comment.presentation;

import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getPageCommentsList(
            @PathVariable Long postId,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size
    ) {
        Page<CommentResponse> pagedComments = commentService.getCommentListByPostId(postId, page, size);
        return ResponseEntity.ok(pagedComments);
    }
}
