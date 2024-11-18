package com.ssafeople.backend.domain.post.presentation;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.post.service.PostService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostSummaryResponse>> getPostsByBoard(@PathVariable(name="boardId") Short boardId) {
        List<PostSummaryResponse> posts = postService.getPostsByBoardId(boardId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Void> writePost(
            @PathVariable(name="boardId") Short boardId,
            @Valid @RequestBody PostWriteRequest request, @AuthenticationPrincipal String email
    ) {
        User user = userService.getUser(email);
        postService.writePost(request, boardId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
