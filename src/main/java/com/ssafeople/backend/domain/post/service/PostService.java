package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;

import java.util.List;

public interface PostService {
    List<PostSummaryResponse> getPostsByBoardId(Short boardId);
    void writePost(PostWriteRequest request, Short boardId, User user);
}
