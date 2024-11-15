package com.ssafeople.backend.domain.post.presentation;

import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.service.PostService;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/post-list")
    public ResponseEntity<List<Post>> getPostsByBoard(@RequestParam(required = true) Short boardId) {
        List<Post> posts = null;
        try {
            posts = postService.getPostsByBoardId(boardId);
        } catch (PostListEmptyException e) {
            log.error("이 게시판은 글이 없네");
        }
        return ResponseEntity.ok(posts);
    }

}
