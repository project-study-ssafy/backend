package com.ssafeople.backend.domain.like.service;


import com.ssafeople.backend.domain.user.domain.User;

public interface LikeService {
    void doLikePost(User user, Long postId);
    void undoLikePost(User user, Long postId);
}
