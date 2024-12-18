package com.ssafeople.backend.domain.like.domain.repository;

import com.ssafeople.backend.domain.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostId(Short userId, Long postId);
}
