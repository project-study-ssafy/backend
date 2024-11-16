package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.post.domain.vo.PostInfoVO;

import java.util.List;

public interface PostService {
    List<PostInfoVO> getPostsByBoardId(Short boardId);
}
