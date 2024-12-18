package com.ssafeople.backend.domain.like.service;

import com.ssafeople.backend.domain.like.domain.Like;
import com.ssafeople.backend.domain.like.domain.repository.LikeRepository;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.like.AlreadyLikedException;
import com.ssafeople.backend.global.exception.like.LikeNotExistsException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    public void doLikePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotExistException::new);

        Like like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if (like != null) {
            throw AlreadyLikedException.EXCEPTION;
        }

        Like newLike = new Like(user, post);
        likeRepository.save(newLike);
    }

    @Override
    public void undoLikePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotExistException::new);

        Like like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if (like == null) {
            throw LikeNotExistsException.EXCEPTION;
        }
        likeRepository.delete(like);
    }
}
