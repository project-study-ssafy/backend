package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getPostsByBoardId(Short boardId) {
        List<Post> posts = postRepository.findByBoardId(boardId);

        if (posts.isEmpty()) {
            throw PostListEmptyException.EXCEPTION;
        }

        return posts;
    }
}
