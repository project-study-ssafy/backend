package com.ssafeople.backend.domain.like.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.like.domain.repository.LikeRepository;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.like.AlreadyLikedException;
import com.ssafeople.backend.global.exception.like.LikeNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LikeServiceImplTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    Board testBoard;
    User testUser;
    Post post;

    @BeforeEach
    void setUp() {
        testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);

        post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);
    }

    @Test
    @DisplayName("좋아요 성공")
    void like_success() {
        likeService.doLikePost(testUser, post.getId());
        assertNotNull(likeRepository.findByUserIdAndPostId(testUser.getId(), post.getId()));
    }

    @Test
    @DisplayName("좋아요 실패 (이미 좋아요가 존재함) 예외 발생")
    void like_fail() {
        likeService.doLikePost(testUser, post.getId());

        assertThrows(AlreadyLikedException.class, () -> likeService.doLikePost(testUser, post.getId()));
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void unlike_success() {
        likeService.doLikePost(testUser, post.getId());
        likeService.undoLikePost(testUser, post.getId());

        assertNull(likeRepository.findByUserIdAndPostId(testUser.getId(), post.getId()));
    }

    @Test
    @DisplayName("좋아요 취소 실패 (좋아요가 없음) 예외 발생")
    void unlike_fail() {
        assertThrows(LikeNotExistsException.class, () -> likeService.undoLikePost(testUser, post.getId()));
    }
}
