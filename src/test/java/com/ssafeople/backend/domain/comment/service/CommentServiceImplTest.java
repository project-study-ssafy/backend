package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.comment.domain.repository.CommentRepository;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.comment.CommentListEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommentServiceImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardRepository boardRepository;

    Board testBoard, testBoard1;
    Post testPost, testPost1;
    User testUser, testUser1;

    @BeforeEach
    void setUp() {
        testBoard = new Board("TEST", "TEST BOARD");
        testBoard1 = new Board("TEST1", "TEST BOARD 1");
        boardRepository.save(testBoard);
        boardRepository.save(testBoard1);

        testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        testUser1 = new User("testUserName1", "test123@test.test1", "", "TestUser1NickName");
        userRepository.save(testUser);
        userRepository.save(testUser1);

        testPost = new Post("testPostTitle", "testPostContent", testUser, testBoard);
        testPost1 = new Post("testPostTitle1", "testPostContent1", testUser1, testBoard1);
        postRepository.save(testPost);
        postRepository.save(testPost1);
    }

    @Test
    @DisplayName("댓글 목록 반환 성공")
    @Transactional(readOnly = true)
    void getCommentListByPostId_success() {
        //Given
        Comment comment = new Comment("Test Content", testPost, testUser);
        commentRepository.save(comment);

        //When
        Page<CommentResponse> commentList = commentService.getCommentListByPostId(testPost.getId(), 1, 10);

        //Then
        assertThat(commentList.get().toList().get(0).getContent()).isEqualTo(comment.getContent());
    }

    @Test
    @DisplayName("댓글 목록 반환 실패 시 (댓글 목록이 비어있음) 예외 반생")
    @Transactional(readOnly = true)
    void getCommentListByPostId_fail() {
        //When & Then
        assertThrows(CommentListEmptyException.class, () -> commentService.getCommentListByPostId(testPost.getId(), 999, 10));
    }
}
