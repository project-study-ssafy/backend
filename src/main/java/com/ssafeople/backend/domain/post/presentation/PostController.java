package com.ssafeople.backend.domain.post.presentation;

import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostSummaryResponse>> getPostsByBoard(@PathVariable(name="boardId") Short boardId) {
        List<PostSummaryResponse> posts = postService.getPostsByBoardId(boardId);
        return ResponseEntity.ok(posts);
    }
}
