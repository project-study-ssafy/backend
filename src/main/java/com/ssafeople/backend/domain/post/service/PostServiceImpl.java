package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
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
                            .userName(post.getUser().getUsername())
                            .createdAt(post.getCreatedAt())
                            .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional
    public void writePost(PostWriteRequest request, Short boardId, User user) {

        Board board = boardService.getBoardById(boardId);

        Post post = new Post(request.getTitle(), request.getContent(), user, board);

        postRepository.save(post);
    }
}
