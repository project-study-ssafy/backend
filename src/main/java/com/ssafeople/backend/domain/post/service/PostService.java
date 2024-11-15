package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.domain.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByBoardId(Short boardId);
}
