package com.ssafeople.backend.domain.post.presentation;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostUpdateRequest;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.post.service.PostService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.utils.user.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {

    private final PostService postService;
    private final UserUtils userUtils;

    @GetMapping
    public ResponseEntity<Page<PostSummaryResponse>> getPagedPostsByBoardId(
            @PathVariable(name="boardId") Short boardId,
            @RequestParam(required = false, defaultValue = "1", value = "page") int pageNumber,
            @RequestParam(required = false, defaultValue = "15", value = "size") int pageSize
    ) {
        Page<PostSummaryResponse> pagedPosts = postService.getPagedPostsByBoardId(boardId, pageNumber, pageSize);
        return ResponseEntity.ok(pagedPosts);
    }

    @GetMapping("/allPosts")
    public ResponseEntity<List<PostSummaryResponse>> getAllPostsByBoardId(
            @PathVariable(name="boardId") Short boardId
    ) {
        List<PostSummaryResponse> posts = postService.getPostsByBoardId(boardId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(
            @PathVariable(name="postId") Long postId
    ) {
        PostDetailResponse response = postService.getPostById(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> writePost(
            @PathVariable(name="boardId") Short boardId,
            @Valid @RequestBody PostWriteRequest request
    ) {
        User user = userUtils.getCurrentUser();
        postService.writePost(request, boardId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable(name = "postId") Long postId,
        @Valid @RequestBody PostUpdateRequest request
    ) {
        User user = userUtils.getCurrentUser();
        postService.updatePost(request, postId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        User user = userUtils.getCurrentUser();
        postService.deletePost(postId, user);
        return ResponseEntity.noContent().build();
    }

}
