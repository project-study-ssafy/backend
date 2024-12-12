package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostUpdateRequest;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<PostSummaryResponse> getPostsByBoardId(Short boardId);
    void writePost(PostWriteRequest request, Short boardId, User user);
    void updatePost(PostUpdateRequest request, Long postId, User user);
    void deletePost(Long postId, User user);

    PostDetailResponse getPostById(Long postId);
    Page<PostSummaryResponse> getPagedPostsByBoardId(Short boardId, int page, int size);
}
