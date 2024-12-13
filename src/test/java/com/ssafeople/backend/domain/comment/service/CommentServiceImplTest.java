package com.ssafeople.backend.domain.comment.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.comment.domain.repository.CommentRepository;
import com.ssafeople.backend.domain.comment.presentation.dto.request.CommentWriteRequest;
import com.ssafeople.backend.domain.comment.presentation.dto.response.CommentResponse;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.comment.CommentListEmptyException;
import com.ssafeople.backend.global.exception.comment.CommentNotExistException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import com.ssafeople.backend.global.exception.user.CommentOwnerIsNotCurrentUserException;
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

    @Test
    @DisplayName("댓글 작성 성공")
    void writeComment_success() {
        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setContent("Test Content");
        commentService.writeComment(commentWriteRequest, testUser, testPost.getId());

        Page<CommentResponse> responses = commentService.getCommentListByPostId(testPost.getId(), 1, 10);
        assertThat(responses.get().toList().get(0).getContent()).isEqualTo(commentWriteRequest.getContent());
    }

    @Test
    @DisplayName("댓글 작성 실패 (존재하지 않는 Post에 작성) 예외를 반환한다.")
    void writeComment_fail() {
        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setContent("Test Content");

        Long p = postRepository.findAll().get(0).getId();
        postRepository.deleteAll();

        assertThrows(PostNotExistException.class, () -> commentService.writeComment(commentWriteRequest, testUser, p));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        Comment comment = new Comment("Test Content", testPost, testUser);
        commentRepository.save(comment);

        Long id = comment.getId();
        commentService.deleteComment(testUser, comment.getId());

        Comment c = commentRepository.findById(id).orElse(null);
        assertNull(c);
    }

    @Test
    @DisplayName("댓글 삭제 실패 (존재하지 않는 Comment 삭제) 예외를 반환한다.")
    void deleteComment_fail() {
        assertThrows(CommentNotExistException.class, () -> commentService.deleteComment(testUser, 999L));
    }

    @Test
    @DisplayName("댓글 삭제 실패 (소유권이 없는 User가 삭제 요청) 예외를 반환한다.")
    void deleteComment_fail2() {
        Comment comment = new Comment("Test Content", testPost, testUser);
        commentRepository.save(comment);

        assertThrows(CommentOwnerIsNotCurrentUserException.class, () -> commentService.deleteComment(testUser1, comment.getId()));
    }
}
