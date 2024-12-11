package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostUpdateRequest;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import com.ssafeople.backend.global.exception.user.PostOwnerIsNotCurrentUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final BoardService boardService;

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getPostsByBoardId(Short boardId) {
        List<Post> posts = postRepository.findByBoardId(boardId);

        if (posts.isEmpty()) {
            throw PostListEmptyException.EXCEPTION;
        }

        List<PostSummaryResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            PostSummaryResponse response =
                    PostSummaryResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickName(post.getUser().getNickname())
                            .createdAt(post.getCreatedAt())
                            .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponse getPostById(Long postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);
        return PostDetailResponse
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickname())
                .build();
    }

    @Override
    public void writePost(PostWriteRequest request, Short boardId, User user) {

        Board board = boardService.getBoardById(boardId);
        Post post = new Post(request.getTitle(), request.getContent(), user, board);
        postRepository.save(post);
    }

    @Override
    public void updatePost(PostUpdateRequest request, Long postId, User user) {

        Post post = postRepository.findPostById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        if (!(post.getUser().equals(user))) {
           throw PostOwnerIsNotCurrentUserException.EXCEPTION;
        }

        post.update(request.getTitle(), request.getContent());
    }

    @Override
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        if (!(post.getUser().equals(user))) {
            throw PostOwnerIsNotCurrentUserException.EXCEPTION;
        }

        postRepository.delete(post);
    }
}
