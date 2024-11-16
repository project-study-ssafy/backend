package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;

import java.util.List;

public interface PostService {
    List<PostSummaryResponse> getPostsByBoardId(Short boardId);
}
