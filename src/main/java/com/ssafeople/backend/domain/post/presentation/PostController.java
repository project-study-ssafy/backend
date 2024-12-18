package com.ssafeople.backend.domain.post.presentation;

import com.ssafeople.backend.domain.like.service.LikeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;
    private final UserUtils userUtils;

    @GetMapping
    public Page<PostSummaryResponse> getPagedPostsByBoardId(
            @PathVariable(name="boardId") Short boardId,
            @RequestParam(required = false, defaultValue = "1", value = "page") int pageNumber,
            @RequestParam(required = false, defaultValue = "15", value = "size") int pageSize
    ) {
        return postService.getPagedPostsByBoardId(boardId, pageNumber, pageSize);
    }

    @GetMapping("/allPosts")
    public List<PostSummaryResponse> getAllPostsByBoardId() {
        return postService.getPostsRegardlessBoardId();
    }

    @GetMapping("/{postId}")
    public PostDetailResponse getPost(
            @PathVariable(name="postId") Long postId
    ) {
        return postService.getPostById(postId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void writePost(
            @PathVariable(name="boardId") Short boardId,
            @Valid @RequestBody PostWriteRequest request
    ) {
        User user = userUtils.getCurrentUser();
        postService.writePost(request, boardId, user);
    }

    @PatchMapping("/{postId}")
    public void updatePost(
        @PathVariable(name = "postId") Long postId,
        @Valid @RequestBody PostUpdateRequest request
    ) {
        User user = userUtils.getCurrentUser();
        postService.updatePost(request, postId, user);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(
            @PathVariable(name = "postId") Long postId
    ) {
        User user = userUtils.getCurrentUser();
        postService.deletePost(postId, user);
    }

    @PostMapping("/{postId}/like")
    public void doLikePost(
            @PathVariable(name = "postId") Long postId
    ) {
        User user = userUtils.getCurrentUser();

        likeService.doLikePost(user, postId);
    }

    @PostMapping("/{postId}/unlike")
    public void doUnLikePost(
            @PathVariable(name = "postId") Long postId
    ) {
        User user = userUtils.getCurrentUser();

        likeService.undoLikePost(user, postId);
    }
}
